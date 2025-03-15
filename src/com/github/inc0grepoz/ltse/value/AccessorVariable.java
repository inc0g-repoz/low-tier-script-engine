package com.github.inc0grepoz.ltse.value;

import java.lang.reflect.Array;

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
        return elementIndex == null ? rv : Array.get(rv,
                (int) elementIndex.linkedAccess(ctx, null));
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

        Object rv = ctx.getVariable(name);

        if (next == null)
        {
            int idx = (int) elementIndex.linkedAccess(ctx, null);
            Array.set(rv, idx, val = convert(val));
            return val;
        }

        return next.mutate(ctx, access(ctx, src), val);
    }

}
