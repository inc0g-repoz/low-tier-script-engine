package com.github.inc0grepoz.ltse.unit.expression;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;
import com.github.inc0grepoz.ltse.value.AccessorVariable;

public class OperatorFunctionProxy extends Operator
{

    public OperatorFunctionProxy(String name)
    {
        super(name, OperatorType.BINARY);
    }

    @Override
    public Object evaluate(ExecutionContext ctx, Accessor[] operands)
    {
        if (operands[0] != Accessor.THIS)
        {
            throw new RuntimeException("Illegal operand " + operands[0]);
        }

        if (!(operands[1] instanceof AccessorVariable))
        {
            throw new RuntimeException("Illegal function name " + operands[1]);
        }

        return ((AccessorVariable) operands[1]).getName();
    }

}
