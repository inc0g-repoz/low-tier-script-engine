package com.github.inc0grepoz.ltse.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ast.ASTNode;

public class UnitFunction extends UnitSection
{

    static UnitFunction compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();

        tokens.poll(); // function
        String name = tokens.poll();
        String token = tokens.poll();

        if (!token.equals("("))
        {
            throw new IllegalStateException("'(' expected, but \"" + token + "\" found");
        }

        token = tokens.poll();

        if (token.equals(")"))
        {
            return new UnitFunction(section, name, Collections.emptyList());
        }

        List<String> paramNames = new ArrayList<>();
        paramNames.add(token);

        while ((token = tokens.poll()).equals(","))
        {
            paramNames.add(tokens.poll());
        }

        if (!token.equals(")"))
        {
            throw new IllegalStateException("')' expected, but \"" + token + "\" found");
        }

        UnitFunction unit = new UnitFunction(section, name, paramNames);

        if (!tokens.isEmpty())
        {
            ScriptCompiler.compileUnit_r(script, node, unit);
        }

        return unit;
    }

    final String name;
    final List<String> paramNames;

    UnitFunction(UnitSection parent, String name, List<String> paramNames)
    {
        super(parent);
        this.name = name;
        this.paramNames = paramNames;
    }

}
