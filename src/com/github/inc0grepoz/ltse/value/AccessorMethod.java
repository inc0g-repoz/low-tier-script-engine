package com.github.inc0grepoz.ltse.value;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

public class AccessorMethod extends AccessorNamed
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
                if (params[i] == null)
                {
                    continue;
                }

                paramList.add(puw = params[i].linkedAccess(ctx, null));
                classList.add(puw.getClass());
            }

            paramList.toArray(paramArr = new Object[paramList.size()]);
            classList.toArray(classArr = new Class<?>[classList.size()]);
        }

        Class<?> clazz = src.getClass();

        if (cachedType != clazz)
        {
            cachedMethod = findMethod(cachedType = clazz, name, paramArr, classArr);
        }

        try
        {
            return access(cachedMethod, src, paramArr);
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
        return next == null
                ? super.mutate(ctx, src, val)
                : next.mutate(ctx, access(ctx, src), val);
    }

    private Method findMethod(Class<?> clazz, String name, Object[] params, Class<?>[] classes)
    {
        try
        {
            return clazz.getMethod(name, classes);
        }
        catch (Throwable t1)
        {
            try
            {
                return clazz.getMethod(name, classes);
            }
            catch (Throwable t2)
            {}
        }

        for (Method next: clazz.getMethods())
        {
            if (next.getParameterCount() == params.length && next.getName().equals(name))
            {
                return next;
            }
        }

        for (Method next: clazz.getDeclaredMethods())
        {
            if (next.getParameterCount() == params.length && next.getName().equals(name))
            {
                return next;
            }
        }

        return null;
    }

}
