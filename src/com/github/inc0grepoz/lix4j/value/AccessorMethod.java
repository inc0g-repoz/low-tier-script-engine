package com.github.inc0grepoz.lix4j.value;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.github.inc0grepoz.lix4j.unit.ExecutionContext;
import com.github.inc0grepoz.lix4j.unit.UnitFunction;
import com.github.inc0grepoz.lix4j.util.PrimitiveConverter;
import com.github.inc0grepoz.lix4j.util.Reflection;

class AccessorMethod extends AccessorNamed
{

    private final Accessor[] params;

    private Class<?> cachedType;
    private Method cachedMethod;

    AccessorMethod(String name, Accessor[] params)
    {
        super(name);
        this.params = params;
    }

    @Override
    public String toString()
    {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");

        for (int i = 0; i < params.length; i++)
        {
            joiner.add(params[i].toString());
        }

        return name + joiner.toString() + (next == null ? "" : "." + next);
    }

    @Override
    public Object access(ExecutionContext ctx, Object src)
    {
        Object[] paramArr = cacheMethod(ctx, src);

        try
        {
            Object rv = access(cachedMethod, src, paramArr);
            return elementIndex == null ? rv : accessElement(ctx, rv);
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to invoke the method " + this, t);
        }
    }

    Object access(Method method, Object src, Object[] params)
    throws Throwable
    {
        boolean accessible = method.isAccessible();
        Object rv = null;

        method.setAccessible(true);
        rv = method.invoke(src, params);

        if (!accessible)
        {
            method.setAccessible(accessible);
        }

        return rv;
    }

    @Override
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        if (elementIndex == null)
        {
            return next == null
                    ? super.mutate(ctx, src, val)
                    : next.mutate(ctx, access(ctx, src), val);
        }

        Object[] paramArr = cacheMethod(ctx, src);

        try
        {
            return mutateElement(ctx, access(cachedMethod, src, paramArr), val);
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to mutate a return value element of method " + this, t);
        }
    }

    private Object[] cacheMethod(ExecutionContext ctx, Object src)
    {
        Object[]   paramArr;
        Class<?>[] classArr;

        if (params == null || params.length == 0)
        {
            paramArr = new Object[0];
            classArr = new Class<?>[0];
        }
        else
        {
            List<Object>   paramList = new ArrayList<>();
            List<Class<?>> classList = new ArrayList<>();
            Object puw;

            for (int i = 0; i < params.length; i++)
            {
                if (params[i] == Accessor.NULL)
                {
                    paramList.add(null);
                    classList.add(Object.class);
                }
                else
                {
                    paramList.add(puw = params[i].linkedAccess(ctx, null));
                    classList.add(puw.getClass());
                }
            }

            paramList.toArray(paramArr = new Object[paramList.size()]);
            classList.toArray(classArr = new Class<?>[classList.size()]);
        }

        Class<?> clazz = unwrapSourceType(src);

        if (cachedType != clazz)
        {
            cachedMethod = Reflection.findMethod(cachedType = clazz, name, paramArr, classArr);
            handleFunctionReferences(cachedMethod, ctx, paramArr, classArr);
        }

        handlePrimitivesMismatch(paramArr, cachedMethod.getParameterTypes());
        return paramArr;
    }

    private void handleFunctionReferences(Method method, ExecutionContext ctx,
            Object[] paramArray, Class<?>[] classArray)
    {
        for (int i = 0; i < paramArray.length; i++)
        {
            if (paramArray[i] == null)
            {
                continue;
            }

            if (paramArray[i].getClass() == AccessorFunctionProxy.class)
            {
                AccessorFunctionProxy accessor = (AccessorFunctionProxy) paramArray[i];
                Class<?> parameterType = method.getParameterTypes()[i];

                if (Reflection.isFunctionalInterface(parameterType))
                {
                    String fnName = accessor.getName();
                    int paramCount = parameterType.getTypeParameters().length;
                    UnitFunction fn = ctx.getScript().getFunction(fnName, paramCount);

                    if (fn == null)
                    {
                        throw new RuntimeException("Unknown function " + fnName + " with " + paramCount + " parameter(-s)");
                    }

                    Object proxy = accessor.initProxy(fn, parameterType);

                    // Writing function cache
                    params[i] = new AccessorValue(proxy)
                    {

                        @Override
                        public String toString()
                        {
                            return accessor.toString();
                        }

                    };

                    paramArray[i] = proxy;
                    classArray[i] = parameterType;
                }
            }
        }
    }

    private void handlePrimitivesMismatch(Object[] paramArray, Class<?>[] classArray)
    {
        for (int i = 0; i < paramArray.length; i++)
        {
            paramArray[i] = PrimitiveConverter.convert(paramArray[i], classArray[i]);
        }
    }

}
