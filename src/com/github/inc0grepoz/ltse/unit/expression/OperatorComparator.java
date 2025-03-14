package com.github.inc0grepoz.ltse.unit.expression;

import java.util.function.BiFunction;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;

public class OperatorComparator extends Operator
{

    private final BiFunction<Number, Number, Boolean> lambda;

    public OperatorComparator(String name, BiFunction<Number, Number, Boolean> lambda)
    {
        super(name, 2);
        this.lambda = lambda;
    }

    @Override
    public Object evaluate(ExecutionContext ctx, Accessor[] operands)
    {
        if (operands.length != operandCount)
        {
            throw new RuntimeException("Only can compare " + operandCount + " numbers");
        }

        Number fst = (Number) operands[0].linkedAccess(ctx, null);
        Number scd = (Number) operands[1].linkedAccess(ctx, null);

        return lambda.apply(fst, scd);
    }

}
