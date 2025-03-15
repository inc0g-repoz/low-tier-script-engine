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

/**
 * A script execution framework.
 * 
 * @author inc0g-repoz
 */
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
        operators.add(new OperatorAssignMutateUnary("--", OperatorType.UNARY_LEFT,  (n) -> n.doubleValue() - 1));
        operators.add(new OperatorAssignMutateUnary("++", OperatorType.UNARY_RIGHT, (n) -> n.doubleValue() + 1));
        operators.add(new OperatorAssignMutateUnary("--", OperatorType.UNARY_RIGHT, (n) -> n.doubleValue() - 1));
    }

    /**
     * Loads a script from the reader input, compiles it,
     * stores it in the pool and returns it's instance.
     * 
     * @param reader the character stream reader
     * @return a compiled {@code Script}
     * @throws IOException
     *         if one of the input operations interrupts
     *         during compilation
     */
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

    /**
     * Loads a script from the file content, compiles it,
     * stores it in the pool and returns it's instance.
     * 
     * @param file the file to read characters from
     * @return a compiled {@code Script}
     * @throws IOException
     *         if one of the input operations interrupts
     *         during compilation
     */
    public Script load(File file) throws IOException
    {
        return load(new FileReader(file));
    }

    /**
     * Removes the first occurrence of the specified script
     * from the pool, if it is present. Returns {@code true},
     * if it was stored in the pool, or {@code false} otherwise.
     * 
     * @param script the script to unload
     * @return whether the specified script was stored in the pool
     */
    public boolean unload(Script script)
    {
        return scripts.remove(script);
    }

    /**
     * Unloads all scripts from the pool.
     */
    public void unloadAll()
    {
        scripts.clear();
    }

    // Supplies the default operators to compiled scripts
    List<Operator> getDefaultOperators()
    {
        return operators;
    }

}
