package com.github.inc0grepoz.ltse.unit.expression;

import com.github.inc0grepoz.common.util.PrimitiveTester;
import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;

public class OperatorAnd extends Operator
{

    public OperatorAnd(String name)
    {
        super(name, OperatorType.BINARY);
    }

    @Override
    public Object evaluate(ExecutionContext ctx, Accessor[] operands) {
        for (int i = 0; i < operands.length; i++)
        {
            if (PrimitiveTester.isDefaultValue(operands[i].linkedAccess(ctx, null)))
            {
                return false;
            }
        }

        return true;
    }

}
