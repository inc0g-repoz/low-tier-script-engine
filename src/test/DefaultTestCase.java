package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.function.Supplier;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ScriptExecutor;
import com.github.inc0grepoz.ltse.util.Lexer;

@SuppressWarnings("all")
class DefaultTestCase {

    private static final ScriptExecutor EXECUTOR = new ScriptExecutor();
    private static final File LOADER_DIRECTORY;

    static
    {
        EXECUTOR.setLoaderDirectory(LOADER_DIRECTORY = new File("scripts"));
    }

    @Disabled
    @Test
    void testLexer()
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
            fail("Failed to lex the script: " + t);
        }
    }

    //@Disabled
    @Test
    void testScriptEngine()
    {
        File file = new File(LOADER_DIRECTORY, "main.script");

        Script script = time("Compiled", () -> {
            try
            {
                return EXECUTOR.load(file);
            }
            catch (IOException e)
            {
                throw new AssertionError(e);
            }
        });

        Object rv = time("Executed", () -> script.callFunction("main"));
    }

    @Disabled
    @Test
    void testReflection()
    {
        try
        {
            Object string = "value";
            Object[] args = { "a", "4" };
            Method m = string.getClass().getMethod("replace", CharSequence.class, CharSequence.class);
            Object rv = m.invoke(string, args);
            System.out.println(rv);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new AssertionError(t);
        }
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
