package com.github.inc0grepoz.lix4j.unit;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;
import com.github.inc0grepoz.lix4j.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.lix4j.util.FlowControl;
import com.github.inc0grepoz.lix4j.value.Accessor;

public class UnitOperation extends Unit
{

    static UnitOperation compile(Script script, ASTNode node, UnitSection parent)
    {
        return new UnitOperation(parent, ExpressionResolver.resolve(script, parent, node.getTokens()));
    }

    private Accessor accessor;

    UnitOperation(UnitSection parent, Accessor accessor) {
        super(parent);
        this.accessor = accessor;
    }

    @Override
    public String toString()
    {
        return accessor.toString();
    }

    @Override
    Object execute(ExecutionContext context)
    {
        accessor.linkedAccess(context, null);
        return FlowControl.KEEP_EXECUTING;
    }

}
