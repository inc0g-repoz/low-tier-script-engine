package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.commons.util.json.mapper.PrimitiveTester;
import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.SyntaxError;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.ltse.value.Accessor;

public class UnitConditionalFor extends UnitSection
{

    static UnitConditionalFor compile(Script script, ASTNode node, UnitSection section)
    {
        node.getTokens().poll(); // for

        LinkedList<String> tempTokens = node.readEnclosedTokens("(", ")");
        ExpressionResolver.openParentheses(tempTokens);
        LinkedList<LinkedList<String>> split = ExpressionResolver.splitTokens(tempTokens, ";");

        if (split.size() != 3)
        {
            throw new SyntaxError("Bad for-loop syntax");
        }

        Accessor parameter = (tempTokens = split.poll()).isEmpty() ? null
                : ExpressionResolver.resolve(script, tempTokens);
        Accessor condition = (tempTokens = split.poll()).isEmpty() ? null
                : ExpressionResolver.resolve(script, tempTokens);
        Accessor increment = (tempTokens = split.poll()).isEmpty() ? null
                : ExpressionResolver.resolve(script, tempTokens);

        UnitConditionalFor unit = new UnitConditionalFor(section, parameter, condition, increment);
        ScriptCompiler.appendSectionUnits(script, node, unit);

        return unit;
    }

    final Accessor parameter, condition, increment;

    UnitConditionalFor(UnitSection parent, Accessor parameter, Accessor condition, Accessor increment)
    {
        super(parent);
        this.parameter = parameter;
        this.condition = condition;
        this.increment = increment;
    }

    @Override
    public String toString()
    {
        return "while (" + condition + ") " + super.toString();
    }

    @Override
    Object execute(ExecutionContext context)
    {
        if (parameter != null)
        {
            parameter.linkedAccess(context, null);
        }

        while (condition == null || !PrimitiveTester.isDefaultValue(condition.linkedAccess(context, null)))
        {
            Object rv = super.execute(context);

            if (rv != Script.KEEP_EXECUTING)
            {
                return rv;
            }

            if (increment != null)
            {
                increment.access(context, null);
            }
        }

        return Script.KEEP_EXECUTING;
    }

}
