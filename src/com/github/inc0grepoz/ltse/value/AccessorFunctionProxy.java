package com.github.inc0grepoz.ltse.value;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

class AccessorFunctionProxy extends Accessor
{

    private final String string;
    private final Object proxy;

    AccessorFunctionProxy(String string, Object proxy)
    {
        this.string = string;
        this.proxy = proxy;
    }

    @Override
    public String toString()
    {
        return string;
    }

    @Override
    public Object access(ExecutionContext ctx, Object src)
    {
        return proxy;
    }

}
