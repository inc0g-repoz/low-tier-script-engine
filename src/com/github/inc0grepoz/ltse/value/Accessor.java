package com.github.inc0grepoz.ltse.value;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

public abstract class Accessor
{

    public static AccessorBuilder builder()
    {
        return new AccessorBuilder();
    }

    Accessor next;

    public Object linkedAccess(ExecutionContext ctx, Object src)
    {
        Object rv = access(ctx, src);
        return next == null ? rv : next.linkedAccess(ctx, rv);
    }

    public abstract Object access(ExecutionContext ctx, Object src);

    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        throw new UnsupportedOperationException();
    }

}
