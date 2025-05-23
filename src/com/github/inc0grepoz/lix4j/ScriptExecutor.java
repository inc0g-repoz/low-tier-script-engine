package com.github.inc0grepoz.lix4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.github.inc0grepoz.lix4j.ast.AST;
import com.github.inc0grepoz.lix4j.unit.UnitFunction;
import com.github.inc0grepoz.lix4j.unit.UnitRoot;
import com.github.inc0grepoz.lix4j.unit.expression.Operator;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorAdd;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorAnd;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorAssign;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorAssignMutate;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorAssignMutateUnary;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorComparator;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorDivide;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorEqual;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorFunctionProxy;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorMultiply;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorNot;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorNotEqual;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorOr;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorSubtract;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorTernary;
import com.github.inc0grepoz.lix4j.unit.expression.OperatorType;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltClassForName;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltLength;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltNewArray;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltNewInstance;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltNoInstance;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltPrint;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltPrintln;
import com.github.inc0grepoz.lix4j.unit.inbuilt.InBuiltSleep;
import com.github.inc0grepoz.lix4j.util.Lexer;

/**
 * A script execution framework.
 * 
 * @author inc0g-repoz
 */
public class ScriptExecutor
{

    private final List<Script> scripts = new ArrayList<>();
    private final List<Operator> operators = new ArrayList<>();
    private final List<Function<UnitRoot, UnitFunction>> inbuilt = new ArrayList<>();

    private File loaderDirectory = null;

    {
        operators.add(new OperatorAssign           ("="));
        operators.add(new OperatorTernary          (null));
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
        operators.add(new OperatorFunctionProxy    ("::"));

        inbuilt.add(InBuiltClassForName::new);
        inbuilt.add(InBuiltLength::new);
        inbuilt.add(InBuiltNewArray::new);
        inbuilt.add(InBuiltNewInstance::new);
        inbuilt.add(InBuiltNoInstance::new);
        inbuilt.add(InBuiltPrint::new);
        inbuilt.add(InBuiltPrintln::new);
        inbuilt.add(InBuiltSleep::new);
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
        AST ast = AST.generateTree(input);

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

    /**
     * Sets the root loader directory for scripts to include
     * other script files from.
     * 
     * @param loaderDirectory a loader directory
     */
    public void setLoaderDirectory(File loaderDirectory)
    {
        if (loaderDirectory == null || !loaderDirectory.isDirectory())
        {
            throw new IllegalArgumentException();
        }

        this.loaderDirectory = loaderDirectory;
    }

    // Returns the directory to include scripts from
    public File getLoaderDirectory()
    {
        return loaderDirectory;
    }

    // Supplies the default operators to compiled scripts
    public List<Operator> getDefaultOperators()
    {
        return operators;
    }

    // Supplies the inbuilt functions to compiled scripts
    public void supplyInbuiltFunctions(UnitRoot root)
    {
        inbuilt.forEach(fs -> fs.apply(root));
    }

}
