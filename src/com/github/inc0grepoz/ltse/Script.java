package com.github.inc0grepoz.ltse;

import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.ltse.ast.AST;
import com.github.inc0grepoz.ltse.unit.ScriptCompiler;
import com.github.inc0grepoz.ltse.unit.UnitFunction;
import com.github.inc0grepoz.ltse.unit.UnitRoot;
import com.github.inc0grepoz.ltse.unit.expression.Operator;

public class Script
{

    private final List<Operator> operators = new ArrayList<>();
    private final UnitRoot root;

    Script(ScriptExecutor executor, AST ast)
    {
        executor.getDefaultOperators().forEach(operators::add);
        root = ScriptCompiler.compile(this, ast);
    }

    public UnitFunction getFunction(String name, int paramCount)
    {
        return root.getFunction(name, paramCount);
    }

    public Object callFunction(String name, Object... args)
    {
        return root.getFunction(name, args.length).call(args);
    }

    public List<Operator> getOperators()
    {
        return operators;
    }

}
