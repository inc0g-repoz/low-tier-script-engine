package com.github.inc0grepoz.lix4j.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;
import com.github.inc0grepoz.lix4j.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.lix4j.util.TokenHelper;
import com.github.inc0grepoz.lix4j.value.Accessor;

public class UnitConditionalCatch extends UnitSection
{

    static UnitConditionalCatch compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();
        tokens.poll(); // catch

        LinkedList<String> errorTokens = TokenHelper.readEnclosedTokens(node.getTokens(), "(", ")");
        Accessor error = ExpressionResolver.resolve(script, errorTokens);

        UnitConditionalCatch unit = new UnitConditionalCatch(section, error);

        if (tokens.isEmpty())
        {
            ScriptCompiler.appendSectionUnits(script, node, unit);
        }
        else
        {
            unit.childs.add(ScriptCompiler.compileUnit_r(script, node, section));
        }

        return unit;
    }

    final Accessor error;

    UnitConditionalCatch(UnitSection parent, Accessor error)
    {
        super(parent);
        this.error = error;
    }

    @Override
    public String toString()
    {
        return "catch (" + error + ") " + super.toString();
    }

    public Accessor getError()
    {
        return error;
    }

}
