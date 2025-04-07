package com.github.inc0grepoz.lix4j.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;

public class UnitConditionalElse extends UnitSection
{

    static UnitConditionalElse compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();
        tokens.poll(); // else

        UnitConditionalElse unit = new UnitConditionalElse(section);

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

    UnitConditionalElse(UnitSection parent)
    {
        super(parent);
    }

    @Override
    public String toString()
    {
        return "else " + super.toString();
    }

}
