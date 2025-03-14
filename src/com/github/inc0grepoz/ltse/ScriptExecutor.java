package com.github.inc0grepoz.ltse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.common.util.Lexer;
import com.github.inc0grepoz.ltse.ast.AST;
import com.github.inc0grepoz.ltse.unit.expression.*;

public class ScriptExecutor
{

    private final List<Script> scripts = new ArrayList<>();
    private final List<Operator> operators = new ArrayList<>();

    {
        operators.add(new OperatorAssign           ("="));
        operators.add(new OperatorOr               ("||"));
        operators.add(new OperatorAnd              ("&&"));
        operators.add(new OperatorAssignMutate     ("+=", (n1, n2) -> n1.doubleValue() + n2.doubleValue()));
        operators.add(new OperatorAssignMutate     ("-=", (n1, n2) -> n1.doubleValue() - n2.doubleValue()));
        operators.add(new OperatorAssignMutate     ("*=", (n1, n2) -> n1.doubleValue() * n2.doubleValue()));
        operators.add(new OperatorAssignMutate     ("/=", (n1, n2) -> n1.doubleValue() / n2.doubleValue()));
        operators.add(new OperatorEqual            ("=="));
        operators.add(new OperatorNotEqual         ("!="));
        operators.add(new OperatorComparator       ("<",  (n1, n2) -> n1.doubleValue() <  n2.doubleValue()));
        operators.add(new OperatorComparator       ("<=", (n1, n2) -> n1.doubleValue() <= n2.doubleValue()));
        operators.add(new OperatorComparator       (">",  (n1, n2) -> n1.doubleValue() >  n2.doubleValue()));
        operators.add(new OperatorComparator       (">=", (n1, n2) -> n1.doubleValue() >= n2.doubleValue()));
        operators.add(new OperatorAdd              ("+"));
        operators.add(new OperatorSubtract         ("-"));
        operators.add(new OperatorMultiply         ("*"));
        operators.add(new OperatorDivide           ("/"));
        operators.add(new OperatorNot              ("!"));
        operators.add(new OperatorAssignMutateUnary("++", OperatorType.UNARY_LEFT,  (n) -> n.doubleValue() + 1));
        operators.add(new OperatorAssignMutateUnary("--", OperatorType.UNARY_LEFT,  (n) -> n.doubleValue() + 1));
        operators.add(new OperatorAssignMutateUnary("++", OperatorType.UNARY_RIGHT, (n) -> n.doubleValue() + 1));
        operators.add(new OperatorAssignMutateUnary("--", OperatorType.UNARY_RIGHT, (n) -> n.doubleValue() + 1));
    }

    public Script load(Reader reader) throws IOException
    {
        LinkedList<String> input = Lexer.readTokens(reader);
//      System.out.println(String.join("$", input));

        AST ast = AST.generateTree(input);
//      System.out.println(ast);

        Script script = new Script(this, ast);
        scripts.add(script);

        return script;
    }

    public Script load(File file) throws IOException
    {
        return load(new FileReader(file));
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
