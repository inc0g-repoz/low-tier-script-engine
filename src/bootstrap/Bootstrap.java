package bootstrap;

import java.io.File;
import java.util.function.Supplier;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ScriptExecutor;

public class Bootstrap
{

    public static void main(String[] args) throws Throwable
    {
        ScriptExecutor executor = new ScriptExecutor();

        // src/com/github/inc0grepoz/dsl/util/Lexer.java
        // test
        File file = new File("test");

        Object object = new Object()
        {
            public int field = 2;

            @Override
            public String toString()
            {
                return Integer.toString(field);
            }

        };

        Script script = time("Compiled", () -> executor.load(file));
        Object result = time("Executed", () -> script.callFunction("test", object));

        System.out.println(result);
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
