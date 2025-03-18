package com.github.inc0grepoz.ltse.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.util.FlowControl;

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

        UnitFunction fn = script.getFunction(name, paramNames.size());

        if (fn != null)
        {
            throw new RuntimeException("Function overloading is not supported (duplicate function " + fn.getSignature() + ")");
        }

        fn = new UnitFunction(section, name, paramNames);
        ScriptCompiler.appendSectionUnits(script, node, fn);

        return fn;
    }

    final String name;
    final List<String> paramNames;
    final UnitRoot root;

    protected UnitFunction(UnitSection parent, String name, List<String> paramNames)
    {
        super(parent);
        this.name = name;
        this.paramNames = paramNames;
        root = root();
    }

    @Override
    public String toString()
    {
        return "function " + getSignature() + " " + super.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof UnitFunction))
        {
            return false;
        }

        UnitFunction fn = (UnitFunction) obj;

        return fn.paramNames.size() == paramNames.size()
                && fn.name.equals(name);
    }

    @Override
    Object execute(ExecutionContext context)
    {
        return FlowControl.KEEP_EXECUTING;
    }

    public Object call(Object... params)
    {
        if (params.length != paramNames.size())
        {
            String desc = name + "(" + String.join(", ", paramNames) + ")";
            throw new IllegalArgumentException(desc);
        }

        ExecutionContext context = root.getScript().supplyContext();
        context.enterSection();

        for (int i = 0; i < params.length; i++)
        {
            context.setVariable(paramNames.get(i), params[i]);
        }

        Object rv = super.execute(context);
        context.exitSection();

        return rv == FlowControl.KEEP_EXECUTING ? FlowControl.VOID : rv;
    }

    public String getSignature()
    {
        return name + "(" + String.join(", ", paramNames) + ")";
    }

}
