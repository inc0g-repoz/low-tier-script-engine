package com.github.inc0grepoz.ltse;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.ltse.ast.AST;
import com.github.inc0grepoz.ltse.unit.expression.Operator;
import com.github.inc0grepoz.ltse.unit.expression.OperatorAdd;
import com.github.inc0grepoz.ltse.unit.expression.OperatorAnd;
import com.github.inc0grepoz.ltse.unit.expression.OperatorAssign;
import com.github.inc0grepoz.ltse.unit.expression.OperatorDivide;
import com.github.inc0grepoz.ltse.unit.expression.OperatorEqual;
import com.github.inc0grepoz.ltse.unit.expression.OperatorMultiply;
import com.github.inc0grepoz.ltse.unit.expression.OperatorNotEqual;
import com.github.inc0grepoz.ltse.unit.expression.OperatorOr;
import com.github.inc0grepoz.ltse.unit.expression.OperatorSubtract;
import com.github.inc0grepoz.util.Lexer;

public class ScriptExecutor
{

    private final List<Script> scripts = new ArrayList<>();
    private final List<Operator> operators = new ArrayList<>();

    {
        operators.add(new OperatorAssign("="));
        operators.add(new OperatorEqual("=="));
        operators.add(new OperatorNotEqual("!="));
        operators.add(new OperatorAdd("+"));
        operators.add(new OperatorSubtract("-"));
        operators.add(new OperatorMultiply("*"));
        operators.add(new OperatorOr("||"));
        operators.add(new OperatorDivide("/"));
        operators.add(new OperatorAnd("&&"));
    }

    public Script load(File file)
    {
        LinkedList<String> input;

        try
        {
            input = Lexer.readTokens(new FileReader(file));
            System.out.println(String.join("$", input));
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }

        AST ast = AST.generateTree(input);

        Script script = new Script(this, ast);
        scripts.add(script);

        return script;
    }

    public boolean unload(Script script)
    {
        return scripts.remove(script);
    }

    public void unloadAll()
    {
        scripts.clear();
    }

    List<Operator> getDefaultOperators()
    {
        return operators;
    }

}
