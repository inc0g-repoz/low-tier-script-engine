package com.github.inc0grepoz.lix4j.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;
import com.github.inc0grepoz.lix4j.unit.expression.ExpressionResolver;
import com.github.inc0grepoz.lix4j.util.TokenHelper;
import com.github.inc0grepoz.lix4j.value.Accessor;

public class UnitErrorCatch extends UnitSection
{

    static UnitErrorCatch compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();
        tokens.poll(); // catch

        LinkedList<String> errorTokens = TokenHelper.readEnclosedTokens(node.getTokens(), "(", ")");
        Accessor error = ExpressionResolver.resolve(script, section, errorTokens);

        UnitErrorCatch unit = new UnitErrorCatch(section, error);

        if (tokens.isEmpty())
        {
            ScriptCompiler.appendSectionUnits(script, node, unit);
        }
        else
        {
            ScriptCompiler.compileUnit_r(script, node, unit);
        }

        return unit;
    }

    final Accessor error;

    UnitErrorCatch(UnitSection parent, Accessor error)
    {
        super(parent, false);
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
