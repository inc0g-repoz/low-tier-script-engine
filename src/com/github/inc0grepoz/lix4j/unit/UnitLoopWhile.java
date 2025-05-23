package com.github.inc0grepoz.lix4j.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;
import com.github.inc0grepoz.lix4j.ctx.ExecutionContext;
import com.github.inc0grepoz.lix4j.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.lix4j.util.FlowControl;
import com.github.inc0grepoz.lix4j.util.PrimitiveTester;
import com.github.inc0grepoz.lix4j.util.TokenHelper;
import com.github.inc0grepoz.lix4j.value.Accessor;

public class UnitLoopWhile extends UnitSection
{

    static UnitLoopWhile compile(Script script, ASTNode node, UnitSection section)
    {
        node.getTokens().poll(); // while

        LinkedList<String> conditionTokens = TokenHelper.readEnclosedTokens(node.getTokens(), "(", ")");
        Accessor condition = ExpressionResolver.resolve(script, section, conditionTokens);

        UnitLoopWhile unit = new UnitLoopWhile(section, condition);
        ScriptCompiler.appendSectionUnits(script, node, unit);

        return unit;
    }

    final Accessor condition;

    UnitLoopWhile(UnitSection parent, Accessor condition)
    {
        super(parent);
        this.condition = condition;
    }

    @Override
    public String toString()
    {
        return "while (" + condition + ") " + super.toString();
    }

    @Override
    Object execute(ExecutionContext context)
    {
        while (condition(context))
        {
            Object rv = super.execute(context);

            if (rv == FlowControl.BREAK)
            {
                break;
            }

            if (rv == FlowControl.CONTINUE)
            {
                continue;
            }

            if (rv != FlowControl.KEEP_EXECUTING)
            {
                return rv;
            }
        }

        return FlowControl.KEEP_EXECUTING;
    }

    private boolean condition(ExecutionContext context)
    {
        return !PrimitiveTester.isDefaultValue(condition.linkedAccess(context, null));
    }

}
