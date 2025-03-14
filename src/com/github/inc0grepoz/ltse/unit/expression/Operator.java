package com.github.inc0grepoz.ltse.unit.expression;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;

public abstract class Operator
{

    final String name;
    final int operandCount;

    Operator(String name, int operandCount)
    {
        this.name = name;
        this.operandCount = operandCount;
    }

    public String getName()
    {
        return name;
    }

    public abstract Object evaluate(ExecutionContext ctx, Accessor[] operands);

}
