package com.github.inc0grepoz.ltse.unit.expression;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;

public abstract class Operator
{

    private final String name;

    Operator(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public abstract Object evaluate(ExecutionContext ctx, Accessor[] operands);

}
