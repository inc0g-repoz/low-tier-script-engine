package com.github.inc0grepoz.ltse.value;

import java.lang.reflect.Field;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

public class AccessorField extends AccessorNamed
{

    private Class<?> cachedType;
    private Field cachedField;

    AccessorField(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        return name + (next == null ? "" : "." + next);
    }

    @Override
    public Object access(ExecutionContext ctx, Object src)
    {
        Class<?> clazz = src.getClass();

        if (cachedType != clazz)
        {
            cachedField = findField(cachedType = clazz, name);
        }

        try
        {
            return access(cachedField, src);
        }
        catch (Throwable t)
        {
            throw new RuntimeException("Failed to access the field " + this, t);
        }
    }

    Object access(Field field, Object src) throws Throwable
    {
        boolean accessible = field.isAccessible();
        Object rv = null;

        field.setAccessible(true);
        rv = field.get(src);

        if (!accessible)
        {
            field.setAccessible(accessible);
        }

        return rv;
    }

    @Override
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        Class<?> clazz = src.getClass();

        if (cachedType != clazz)
        {
            cachedField = findField(cachedType = clazz, name);
        }

        try
        {
            return next == null
                    ? mutate(cachedField, src, convert(val))
                    : next.mutate(ctx, access(ctx, src), val);
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    Object mutate(Field field, Object src, Object val) throws Throwable
    {
        boolean accessible = field.isAccessible();

        field.setAccessible(true);
        field.set(src, val);

        if (!accessible)
        {
            field.setAccessible(accessible);
        }

        return val;
    }

    private Field findField(Class<?> clazz, String name)
    {
        try
        {
            return clazz.getField(name);
        }
        catch (Throwable t1)
        {
            try
            {
                return clazz.getDeclaredField(name);
            }
            catch (Throwable t2)
            {
                return null;
            }
        }
    }

}
