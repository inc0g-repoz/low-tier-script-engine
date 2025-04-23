package com.github.inc0grepoz.lix4j.value;

import com.github.inc0grepoz.lix4j.runtime.ExecutionContext;

/**
 * Represents a read-only no-instance accessor for static class
 * members invocation and access.
 * 
 * @author inc0g-repoz
 */
public class AccessorNoInstance extends Accessor
{

    /**
     * Creates a new {@code AccessorNoInstance} from the
     * specified class.
     * 
     * @param clazz a {@code Class} to wrap
     * @return a new {@code AccessorNoInstance}
     */
    public static AccessorNoInstance of(Class<?> clazz)
    {
        return new AccessorNoInstance(clazz);
    }

    final Class<?> clazz;

    AccessorNoInstance(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    @Override
    public String toString()
    {
        if (clazz == null)
        {
            return null;
        }

        return clazz.getName() + (next == null ? "" : "." + next);
    }

    @Override
    public Object access(ExecutionContext ctx, Object src)
    {
        return null;
    }

}
