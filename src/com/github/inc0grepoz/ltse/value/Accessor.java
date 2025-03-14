package com.github.inc0grepoz.ltse.value;

import java.util.function.Function;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;

public abstract class Accessor
{

    public static final Accessor NULL  = AccessorValue.of(null);
    public static final Accessor TRUE  = AccessorValue.of(true);
    public static final Accessor FALSE = AccessorValue.of(false);

    public static AccessorBuilder builder()
    {
        return new AccessorBuilder();
    }

    Accessor next;

    public abstract Object access(ExecutionContext ctx, Object src);

    public Object linkedAccess(ExecutionContext ctx, Object src)
    {
        Object rv = access(ctx, src);
        return next == null ? rv : next.linkedAccess(ctx, rv);
    }

    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        throw new UnsupportedOperationException();
    }

    public Object mutate(ExecutionContext ctx, Object src, Function<Object, Object> fn)
    {
        Object cv = access(ctx, src);
        return next == null
                ? mutate(ctx, src, fn.apply(cv))
                : next.mutate(ctx, src, fn);
    }

}
