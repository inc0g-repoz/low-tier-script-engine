package com.github.inc0grepoz.lix4j.unit.expression;

import com.github.inc0grepoz.lix4j.ctx.ExecutionContext;
import com.github.inc0grepoz.lix4j.value.Accessor;

public class OperatorAssign extends Operator
{

    public OperatorAssign(String name)
    {
        super(name, OperatorType.BINARY);
    }

    @Override
    public Object evaluate(ExecutionContext ctx, Accessor[] operands)
    {
        Object rv = null;

        for (int i = operands.length - 2; 0 <= i; i--)
        {
            rv = operands[i].mutate(ctx, null, operands[i + 1].linkedAccess(ctx, null));
        }

        return rv;
    }

}
