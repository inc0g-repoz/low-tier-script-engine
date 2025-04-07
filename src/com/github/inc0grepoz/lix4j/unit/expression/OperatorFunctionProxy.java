package com.github.inc0grepoz.lix4j.unit.expression;

import com.github.inc0grepoz.lix4j.unit.ExecutionContext;
import com.github.inc0grepoz.lix4j.value.Accessor;
import com.github.inc0grepoz.lix4j.value.AccessorFunctionProxy;
import com.github.inc0grepoz.lix4j.value.AccessorVariable;

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

        String source = operands[0].toString();
        String name = ((AccessorVariable) operands[1]).getName();
        return new AccessorFunctionProxy(source, name);
    }

}
