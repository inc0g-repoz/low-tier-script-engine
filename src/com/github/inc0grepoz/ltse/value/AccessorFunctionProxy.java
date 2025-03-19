package com.github.inc0grepoz.ltse.value;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.unit.UnitFunction;

public class AccessorFunctionProxy extends AccessorNamed
{

    private final String source;

    private Object proxy;

    public AccessorFunctionProxy(String source, String name)
    {
        super(name);
        this.source = source;
    }

    @Override
    public String toString()
    {
        return source + "::" + name;
    }

    @Override
    public Object access(ExecutionContext ctx, Object src)
    {
        return proxy == null ? this : proxy;
    }

    public Object initProxy(UnitFunction fn, Class<?> type)
    {
        if (proxy != null)
        {
            throw new IllegalStateException("Initialized a function proxy twice");
        }

        return this.proxy = fn.createProxy(type);
    }

}
