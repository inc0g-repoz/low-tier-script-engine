package test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.ScriptExecutor;
import com.github.inc0grepoz.ltse.util.FlowControl;
import com.github.inc0grepoz.ltse.util.Reflection;

@SuppressWarnings({"rawtypes", "unused"})
public class Test
{

    public static void main(String[] args)
    {
        testScriptEngine();
//      test();
    }

    private static void test()
    {
        try
        {
            List<String> list = Arrays.asList("1", "2", "3");
            Method method = list.getClass().getMethod("forEach", Consumer.class);
            method.setAccessible(true);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    private static void testScriptEngine()
    {
        ScriptExecutor executor = new ScriptExecutor();

        // src/com/github/inc0grepoz/dsl/util/Lexer.java
        // test
        File file = new File("test.script");
        Supplier[] getter = (Supplier[]) Array.newInstance(Supplier.class, 1);

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

        Object rv = time("Executed", () -> script.callFunction("main"));

        for (int i = 0; i < 10; i++)
        {
            time("Executed", () -> script.callFunction("main"));
        }

        if (rv != FlowControl.VOID)
        {
            System.out.println("Returned " + rv);
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
