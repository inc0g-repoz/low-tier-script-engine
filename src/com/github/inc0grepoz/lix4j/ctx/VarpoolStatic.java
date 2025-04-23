package com.github.inc0grepoz.lix4j.ctx;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.inc0grepoz.lix4j.unit.UnitSection;
import com.github.inc0grepoz.lix4j.value.AccessorVariable;

/**
 * Represents a static pool for variables.
 * 
 * @author inc0g-repoz
 */
public class VarpoolStatic implements Cloneable
{

    private final UnitSection section;

    private Variable[] pool = new Variable[0];

    /**
     * Creates a new static pool for the specified
     * {@code section}.
     * 
     * @param section the {@code UnitSection}
     */
    public VarpoolStatic(UnitSection section)
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
        Variable rv = get0(name);

        if (rv == null)
        {
            pool = Arrays.copyOf(pool, pool.length + 1);
            rv = pool[pool.length - 1] = new Variable(name);
        }

        return AccessorVariable.of(rv);
    }

    /*
     * Returns a variable with the specified name, but doesn't
     * throw an exception.
     */
    private Variable get0(String name)
    {
        for (int i = 0; i < pool.length; i++)
        {
            if (pool[i].getName().equals(name))
            {
                return pool[i];
            }
        }

        if (section.getParent() != null)
        {
            Variable rv = section.getParent().getVarpool().get0(name);
            if (rv != null)
            {
                return rv;
            }
        }

        return null;
    }

}
