package com.github.inc0grepoz.ltse;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused"})
public class Test
{

    public static void main(String[] args)
    {
        ScriptExecutor executor = new ScriptExecutor();

        // src/com/github/inc0grepoz/dsl/util/Lexer.java
        // test
        File file = new File("test");
        Supplier[] getter = (Supplier[]) Array.newInstance(Supplier.class, 1);

        Object hook = new Object()
        {
            PrintStream out = System.out;
            Thread thread = Thread.currentThread();
        };

        Script script = time("Compiled", () -> {
            try
            {
                return executor.load(file);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        time("Executed", () -> script.callFunction("main", hook));
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
