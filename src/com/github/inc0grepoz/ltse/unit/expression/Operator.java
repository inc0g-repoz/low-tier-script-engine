package com.github.inc0grepoz.ltse.unit.expression;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;

public abstract class Operator
{

    final String name;
    final OperatorType type;

    Operator(String name, OperatorType type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public OperatorType getType()
    {
        return type;
    }

    public abstract Object evaluate(ExecutionContext ctx, Accessor[] operands);

}
