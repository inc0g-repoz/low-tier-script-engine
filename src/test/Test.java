package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Supplier;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ScriptExecutor;
import com.github.inc0grepoz.ltse.util.Lexer;
import com.github.inc0grepoz.ltse.util.TokenHelper;

@SuppressWarnings("unused")
public class Test
{

    private static final ScriptExecutor EXECUTOR = new ScriptExecutor();
    private static final File LOADER_DIRECTORY;

    private static final Object TEST_OBJECT = new Object() {

        public int field;

        public void passInt(int value) {
            System.out.println("int value " + value);
        }

    };

    static
    {
        EXECUTOR.setLoaderDirectory(LOADER_DIRECTORY = new File("scripts"));
    }

    public static void main(String[] args)
    {
//      testLexer();
        testScriptEngine();
//      test();
    }

    private static void testLexer()
    {
        // src/com/github/inc0grepoz/dsl/util/Lexer.java
        // main.script
        File file = new File(LOADER_DIRECTORY, "main.script");

        try
        {
            LinkedList<String> tokens = Lexer.readTokens(new FileReader(file));
            tokens.forEach(token -> System.out.print("$" + token));
            System.out.println();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    private static void testScriptEngine()
    {
        File file = new File(LOADER_DIRECTORY, "main.script");

        Script script = time("Compiled", () -> {
            try
            {
                return EXECUTOR.load(file);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        Object rv = time("Executed", () -> script.callFunction("main"));
    }

    private static void test()
    {
        System.out.println("Nothing to test");
    }

    private static <T> T time(Supplier<T> lambda)
    {
        return time(null, lambda);
    }

    private static <T> T time(String label, Supplier<T> lambda)
    {
        long time = System.currentTimeMillis();
        T rv = lambda.get();
        time = System.currentTimeMillis() - time;
        System.out.println((label == null ? "" : label + " in ") + time + " ms");
        return rv;
    }

}
