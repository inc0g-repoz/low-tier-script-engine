package com.github.inc0grepoz.lix4j.value;

import com.github.inc0grepoz.lix4j.ctx.ExecutionContext;
import com.github.inc0grepoz.lix4j.ctx.Variable;

/**
 * Represents an accessor for variables stored in a
 * variable pool during execution of some function.
 * 
 * @author inc0g-repoz
 */
public class AccessorVariable extends AccessorNamed
{

    /**
     * Creates and returns a new accessor for a variable
     * with a specified name.
     * 
     * @param name the variable name
     * @return a new {@code AccessorVariable}
     */
    public static AccessorVariable of(Variable var)
    {
        return new AccessorVariable(var);
    }

    private final Variable variable;

    AccessorVariable(Variable var)
    {
        super(var.getName());
        variable = var;
    }

    @Override
    public String toString()
    {
        return "$" + name + (next == null ? "" : "." + next);
    }

    @Override
    public Object access(ExecutionContext ctx, Object src) {
        Object rv = variable.get();
        return elementIndex == null ? rv : accessElement(ctx, rv);
    }

    @Override
    public Object mutate(ExecutionContext ctx, Object src, Object val)
    {
        if (elementIndex == null)
        {
            return next == null
                    ? variable.set(val)
                    : next.mutate(ctx, variable.get(), val);
        }

        return mutateElement(ctx, variable.get(), val);
    }

}
