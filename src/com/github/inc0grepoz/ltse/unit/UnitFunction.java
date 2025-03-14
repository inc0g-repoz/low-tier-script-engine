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

        List<String> paramNames;

        if ((token = tokens.poll()).equals(")"))
        {
            paramNames = Collections.emptyList();
        }
        else
        {
            (paramNames = new ArrayList<>()).add(token);

            while ((token = tokens.poll()).equals(","))
            {
                paramNames.add(tokens.poll());
            }

            if (!token.equals(")"))
            {
                throw new IllegalStateException("')' expected, but \"" + token + "\" found");
            }
        }

        UnitFunction unit = new UnitFunction(section, name, paramNames);
        ScriptCompiler.appendSectionUnits(script, node, unit);

        return unit;
    }

    final String name;
    final List<String> paramNames;
    final UnitRoot root;

    UnitFunction(UnitSection parent, String name, List<String> paramNames)
    {
        super(parent);
        this.name = name;
        this.paramNames = paramNames;
        root = root();
    }

    @Override
    public String toString()
    {
        return "function " + name + " (" + String.join(", ", paramNames) + ") " + super.toString();
    }

    public Object call(Object... args)
    {
        ExecutionContext context = new ExecutionContext(root.getScript());
        context.enterSection();

        for (int i = 0; i < args.length; i++)
        {
            context.setVariable(paramNames.get(i), args[i]);
        }

        return execute(context);
    }

}
