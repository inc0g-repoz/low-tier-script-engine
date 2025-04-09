package com.github.inc0grepoz.lix4j.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;

public class UnitConditionElse extends UnitSection
{

    static UnitConditionElse compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();
        tokens.poll(); // else

        UnitConditionElse unit = new UnitConditionElse(section);

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

    UnitConditionElse(UnitSection parent)
    {
        super(parent);
    }

    @Override
    public String toString()
    {
        return "else " + super.toString();
    }

}
