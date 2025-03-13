package com.github.inc0grepoz.ltse.value;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

public class AccessorValue extends Accessor
{

    public static AccessorValue of(Object instance)
    {
        return new AccessorValue(instance);
    }

    final Object instance;

    AccessorValue(Object instance)
    {
        this.instance = instance;
    }

    @Override
    public String toString()
    {
        if (instance == null)
        {
            return null;
        }

        String rv = instance.toString();

        if (instance instanceof String)
        {
            rv = '"' + rv + '"';
        }

        return rv + (next == null ? "" : "." + next);
    }

    @Override
    public Object access(ExecutionContext ctx, Object src)
    {
        return instance;
    }

}
