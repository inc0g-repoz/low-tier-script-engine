package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.exception.SyntaxError;
import com.github.inc0grepoz.ltse.util.FlowControl;

public class UnitBreak extends Unit
{

    static UnitBreak compile(Script script, ASTNode node, UnitSection parent)
    {
        LinkedList<String> tokens = node.getTokens();
        tokens.poll(); // break

        if (!tokens.isEmpty())
        {
            throw new SyntaxError("Illegal tokens after a break statement: " + String.join(" ", tokens));
        }

        return new UnitBreak(parent);
    }

    UnitBreak(UnitSection parent)
    {
        super(parent);
    }

    @Override
    public String toString()
    {
        return "break";
    }

    @Override
    Object execute(ExecutionContext context)
    {
        return FlowControl.BREAK;
    }

}
