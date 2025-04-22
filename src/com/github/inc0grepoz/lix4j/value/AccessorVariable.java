package com.github.inc0grepoz.lix4j.value;

import com.github.inc0grepoz.lix4j.unit.ExecutionContext;

/**
 * Represents an accessor for variables stored in a
 * variable pool during execution of some function.
 * 
 * @author inc0g-repoz
 */
public class AccessorVariable extends AccessorNamed
{

    private static final Object UNASSIGNED = new String("<UNASSIGNED>");

    /**
     * Creates and returns a new accessor for a variable
     * with a specified name.
     * 
     * @param name the variable name
     * @return a new {@code AccessorVariable}
     */
    public static AccessorVariable of(String name)
    {
        return new AccessorVariable(name);
    }

    private Object instance = UNASSIGNED;

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
        if (instance == UNASSIGNED)
        {
            throw new RuntimeException("Variable " + name + " is unassigned");
        }

        return elementIndex == null ? instance : accessElement(ctx, instance);
    }

    @Override
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        System.out.println("Mutating " + name + " = " + val);
        if (elementIndex == null)
        {
            return next == null
                    ? instance = val
                    : next.mutate(ctx, instance, val);
        }

        return mutateElement(ctx, instance, val);
    }

}
