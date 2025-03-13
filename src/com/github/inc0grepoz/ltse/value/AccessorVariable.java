package com.github.inc0grepoz.ltse.value;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

public class AccessorVariable extends AccessorNamed
{

    public static AccessorVariable of(String name)
    {
        return new AccessorVariable(name);
    }

    AccessorVariable(String name)
    {
        super(name);
    }

    @Override
    public String toString()
    {
        return "$" + name + (next == null ? "" : "." + next);
    }

    @Override
    public Object access(ExecutionContext ctx, Object src) {
        return ctx.getVariable(name);
    }

    @Override
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        return next == null
                ? ctx.setVariable(name, val)
                : next.mutate(ctx, access(ctx, src), val);
    }

}
