package com.github.inc0grepoz.lix4j.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;
import com.github.inc0grepoz.lix4j.util.FlowControl;

public class UnitConditionalTry extends UnitSection
{

    static UnitConditionalTry compile(Script script, ASTNode node, UnitSection section)
    {
        node.getTokens().poll(); // try

        UnitConditionalTry unit = new UnitConditionalTry(section);
        ScriptCompiler.appendSectionUnits(script, node, unit);

        LinkedList<ASTNode> parentNodes = node.getParent().getChildNodes();
        if (!parentNodes.isEmpty())
        {
            if (parentNodes.peek().getTokens().peek().equals("catch"))
            {
                unit.otherwise = UnitConditionalCatch.compile(script, parentNodes.poll(), null);
            }
        }

        return unit;
    }

    UnitConditionalCatch otherwise;

    UnitConditionalTry(UnitSection parent)
    {
        super(parent);
    }

    @Override
    public String toString()
    {
        return "try " + super.toString() + (otherwise == null ? "" : " " + otherwise);
    }

    @Override
    Object execute(ExecutionContext context)
    {
        try
        {
            return super.execute(context);
        }
        catch (Throwable t)
        {
            if (otherwise == null)
            {
                return FlowControl.KEEP_EXECUTING;
            }

            otherwise.getError().mutate(context, null, t);
            return otherwise.execute(context);
        }
    }

}
