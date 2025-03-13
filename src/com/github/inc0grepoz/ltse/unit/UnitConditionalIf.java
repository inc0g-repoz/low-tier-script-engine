package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.commons.util.json.mapper.PrimitiveTester;
import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.SyntaxError;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.ltse.value.Accessor;

public class UnitConditionalIf extends UnitSection
{

    static UnitConditionalIf compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();

        tokens.poll(); // if
        String token = tokens.peek();

        if (!token.equals("("))
        {
            throw new SyntaxError("'(' expected, but \"" + token + "\" found");
        }

        LinkedList<String> conditionTokens = node.readEnclosedTokens("(", ")");
        Accessor condition = ExpressionResolver.resolve(script, conditionTokens);
        UnitConditionalIf unit = new UnitConditionalIf(section, condition);

        if (!tokens.isEmpty())
        {
            ScriptCompiler.compileUnit_r(script, node, unit);
        }

        return unit;
    }

    final Accessor condition;

    UnitConditionalElse otherwise;

    UnitConditionalIf(UnitSection parent, Accessor condition)
    {
        super(parent);
        this.condition = condition;
    }

    @Override
    Object execute(ExecutionContext context)
    {
        return PrimitiveTester.isDefaultValue(condition.linkedAccess(context, null))
                ? (otherwise == null ? Script.KEEP_EXECUTING : otherwise.execute(context))
                : super.execute(context);
    }

}
