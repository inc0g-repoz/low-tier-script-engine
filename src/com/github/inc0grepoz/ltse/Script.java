package com.github.inc0grepoz.ltse;

import java.util.ArrayList;
import java.util.List;

import com.github.inc0grepoz.ltse.ast.AST;
import com.github.inc0grepoz.ltse.unit.ScriptCompiler;
import com.github.inc0grepoz.ltse.unit.UnitRoot;
import com.github.inc0grepoz.ltse.unit.expression.Operator;

public class Script
{

    /**
     * Used by the script units to continue executing,
     * if one of the units returns a value.
     */
    public static final Object KEEP_EXECUTING = void.class;

    private final List<Operator> operators = new ArrayList<>();
    private final UnitRoot root;

    Script(ScriptExecutor executor, AST ast)
    {
        executor.getDefaultOperators().forEach(operators::add);
        root = ScriptCompiler.compile(this, ast);
    }

    public Object callFunction(String name, Object... args)
    {
        return root.callFunction(name, args);
    }

    public List<Operator> getOperators()
    {
        return operators;
    }

}
