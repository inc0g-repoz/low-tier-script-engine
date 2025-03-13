package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.commons.util.json.mapper.PrimitiveTester;
import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.SyntaxError;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.ltse.value.Accessor;

public class UnitConditionalWhile extends UnitSection
{

    static UnitConditionalWhile compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();

        tokens.poll(); // while
        String token = tokens.peek();

        if (!token.equals("("))
        {
            throw new SyntaxError("'(' expected, but \"" + token + "\" found");
        }

        LinkedList<String> conditionTokens = node.readEnclosedTokens("(", ")");
        Accessor condition = ExpressionResolver.resolve(script, conditionTokens);

        UnitConditionalWhile unit = new UnitConditionalWhile(section, condition);
        ScriptCompiler.appendSectionUnits(script, node, unit);

        return unit;
    }

    final Accessor condition;

    UnitConditionalWhile(UnitSection parent, Accessor condition)
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
        while (!PrimitiveTester.isDefaultValue(condition.linkedAccess(context, null)))
        {
            Object rv = super.execute(context);

            if (rv != Script.KEEP_EXECUTING)
            {
                return rv;
            }
        }

        return Script.KEEP_EXECUTING;
    }

}
