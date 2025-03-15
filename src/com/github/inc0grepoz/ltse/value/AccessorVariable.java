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
        Object rv = ctx.getVariable(name);
        return elementIndex == null ? rv : accessElement(ctx, rv);
    }

    @Override
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        if (elementIndex == null)
        {
            return next == null
                    ? ctx.setVariable(name, convert(val))
                    : next.mutate(ctx, access(ctx, src), val);
        }

        return mutateElement(ctx, ctx.getVariable(name), val);
    }

}
