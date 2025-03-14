package com.github.inc0grepoz.ltse.unit.expression;

import com.github.inc0grepoz.ltse.unit.ExecutionContext;
import com.github.inc0grepoz.ltse.value.Accessor;

public class OperatorSubtract extends Operator
{

    public OperatorSubtract(String name)
    {
        super(name, 2);
    }

    @Override
    public Object evaluate(ExecutionContext ctx, Accessor[] operands) {
        Object object;
        double rv = 0d;

        for (int i = 0; i < operands.length; i++)
        {
            object = operands[i].linkedAccess(ctx, null);

            if (object instanceof Number)
            {
                if (i == 0)
                {
                    rv = ((Number) object).doubleValue();
                }
                else
                {
                    rv -= ((Number) object).doubleValue();
                }
            }
            else
            {
                throw new UnsupportedOperationException("Could not substract from " + object);
            }
        }

        return rv;
    }

}
