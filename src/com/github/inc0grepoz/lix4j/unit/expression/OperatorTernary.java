package com.github.inc0grepoz.lix4j.unit.expression;

import com.github.inc0grepoz.lix4j.runtime.ExecutionContext;
import com.github.inc0grepoz.lix4j.util.PrimitiveTester;
import com.github.inc0grepoz.lix4j.value.Accessor;

public class OperatorTernary extends Operator
{

    public OperatorTernary(String name)
    {
        super(name, OperatorType.TERNARY);
    }

    @Override
    public Object evaluate(ExecutionContext ctx, Accessor[] operands)
    {
        boolean condition = PrimitiveTester.isDefaultValue(operands[0].linkedAccess(ctx, null));
        Object rv1 = operands[1].linkedAccess(ctx, null);
        Object rv2 = operands[2].linkedAccess(ctx, null);
        return condition ? rv2 : rv1;
    }

}
