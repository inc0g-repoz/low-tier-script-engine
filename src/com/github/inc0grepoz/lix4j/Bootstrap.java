package com.github.inc0grepoz.lix4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import com.github.inc0grepoz.lix4j.unit.UnitFunction;

public class Bootstrap
{

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.err.println("No script file specified");
        }
        else
        {
            run(args[0], Arrays.copyOfRange(args, 1, args.length));
        }
    }

    private static void run(String path, String[] args)
    {
        File file = (new File(path)).getAbsoluteFile();
        ScriptExecutor executor = new ScriptExecutor();
        executor.setLoaderDirectory(file.getParentFile());

        try
        {
            Script script = executor.load(file);
            UnitFunction fn;

            if ((fn = script.getFunction("main", 1)) != null) {
                fn.call(Stream.of(args).map(Object.class::cast).toArray(Object[]::new));
            } else if ((fn = script.getFunction("main", 0)) != null) {
                fn.call();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
