package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.exception.SyntaxError;
import com.github.inc0grepoz.ltse.util.FlowControl;

public class UnitContinue extends Unit
{

    static UnitContinue compile(Script script, ASTNode node, UnitSection parent)
    {
        LinkedList<String> tokens = node.getTokens();
        tokens.poll(); // continue

        if (!tokens.isEmpty())
        {
            throw new SyntaxError("Illegal tokens after a continue statement: " + String.join(" ", tokens));
        }

        return new UnitContinue(parent);
    }

    UnitContinue(UnitSection parent)
    {
        super(parent);
    }

    @Override
    public String toString()
    {
        return "continue";
    }

    @Override
    Object execute(ExecutionContext context)
    {
        return FlowControl.CONTINUE;
    }

}
