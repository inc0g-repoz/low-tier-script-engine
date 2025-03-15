package com.github.inc0grepoz.ltse.value;

import java.lang.reflect.Array;
import java.util.function.Function;

import com.github.inc0grepoz.ltse.FlowControl;
import com.github.inc0grepoz.ltse.unit.ExecutionContext;

/**
 * Represents a value, variable or a member access chain.
 * 
 * @author inc0g-repoz
 */
public abstract class Accessor
{

    /** Precached {@code void} return value accessor. **/
    public static final Accessor VOID = AccessorValue.of(FlowControl.VOID);

    /** Precached {@code null} value accessor. **/
    public static final Accessor NULL = AccessorValue.of(null);

    /** Precached void {@code 0} accessor for subtraction operators. **/
    public static final Accessor ZERO = AccessorValue.of(0);

    /** Precached {@code true} value accessor. **/
    public static final Accessor TRUE = AccessorValue.of(true);

    /** Precached {@code false} value accessor. **/
    public static final Accessor FALSE = AccessorValue.of(false);

    /**
     * Creates a new {@code AccessorBuilder} with an empty
     * access chain.
     * 
     * @return a new {@code AccessorBuilder}
     */
    public static AccessorBuilder builder()
    {
        return new AccessorBuilder();
    }

    // Converts objects, if they're integer numbers
    static Object convert(Object object)
    {
        if (object instanceof Number)
        {
            Number n  = (Number) object;
            double dv = n.doubleValue();

            if (dv % 1 == 0)
            {
                if (Byte.MIN_VALUE <= dv && dv <= Byte.MAX_VALUE)
                {
                    return n.byteValue();
                }

                if (Short.MIN_VALUE <= dv && dv <= Short.MAX_VALUE)
                {
                    return n.shortValue();
                }

                if (Integer.MIN_VALUE <= dv && dv <= Integer.MAX_VALUE)
                {
                    return n.intValue();
                }

                return n.longValue();
            }
        }

        return object;
    }

    Accessor next, elementIndex;

    /**
     * Returns an {@code Object} accessed through this {@code Accessor}
     * instance from the specified source to be used in a linked access
     * chain or as a return value.
     * 
     * @param ctx the function {@code ExecutionContext}
     * @param src the {@code Object} source
     * @return an accessed {@code Object}
     */
    public abstract Object access(ExecutionContext ctx, Object src);

    public Object linkedAccess(ExecutionContext ctx, Object src)
    {
        Object rv = access(ctx, src);
        return next == null ? rv : next.linkedAccess(ctx, rv);
    }

    /**
     * Mutates the {@code Object} refered to by this {@code Accessor}
     * and returns the new value specified.
     * 
     * @param ctx the function {@code ExecutionContext}
     * @param src the {@code Object} source
     * @param val the new value
     * @return the new value
     */
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Mutates the {@code Object} refered to by this {@code Accessor}
     * and returns the new value from the specified function.
     * 
     * @param ctx the function {@code ExecutionContext}
     * @param src the {@code Object} source
     * @param fn  the mutator function
     * @return the new value
     */
    public Object mutate(ExecutionContext ctx, Object src, Function<Object, Object> fn)
    {
        Object cv = access(ctx, src);
        return next == null
                ? mutate(ctx, src, fn.apply(cv))
                : next.mutate(ctx, src, fn);
    }

    // Accesses the source object element
    Object accessElement(ExecutionContext ctx, Object eltSrc)
    {
        return Array.get(eltSrc, (int) elementIndex.linkedAccess(ctx, null));
    }

    // Mutates the source object element
    Object mutateElement(ExecutionContext ctx, Object eltSrc, Object val)
    {
        if (next == null)
        {
            int idx = (int) elementIndex.linkedAccess(ctx, null);
            Array.set(eltSrc, idx, val = convert(val));
            return val;
        }

        Object elt = Array.get(eltSrc, (int) elementIndex.linkedAccess(ctx, null));
        return next.mutate(ctx, elt, val);
    }

}
