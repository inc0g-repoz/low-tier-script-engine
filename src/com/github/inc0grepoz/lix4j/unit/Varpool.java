package com.github.inc0grepoz.lix4j.unit;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.inc0grepoz.lix4j.value.AccessorVariable;

/**
 * Represents a pool for variables.
 * 
 * @author inc0g-repoz
 */
public class Varpool implements Cloneable
{

    private final UnitSection section;

    private AccessorVariable[] pool = new AccessorVariable[0];

    /**
     * Creates a new global context for the specified {@code section}.
     * 
     * @param section the {@code UnitSection}
     */
    Varpool(UnitSection section)
    {
        this.section = section;
    }

    @Override
    public String toString()
    {
        return Stream.of(pool).map(Object::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Creates a variable with the specified name or returns
     * the existing one.
     * 
     * @param the variable name
     */
    public AccessorVariable getOrCreate(String name)
    {
        AccessorVariable rv = get0(name);

        if (rv == null)
        {
            pool = Arrays.copyOf(pool, pool.length + 1);
            rv = pool[pool.length - 1] = AccessorVariable.of(name);
        }

        return rv;
    }

    /*
     * Returns a variable with the specified name, but doesn't
     * throw an exception.
     */
    private AccessorVariable get0(String name)
    {
        for (int i = 0; i < pool.length; i++)
        {
            if (pool[i].getName().equals(name))
            {
                return pool[i];
            }
        }

        if (section.parent != null)
        {
            AccessorVariable rv = section.parent.varpool.get0(name);
            if (rv != null)
            {
                return rv;
            }
        }

        return null;
    }

}
