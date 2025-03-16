package com.github.inc0grepoz.ltse.unit;

import java.util.Arrays;

public class InBuiltPrintln extends UnitFunction
{

    InBuiltPrintln(UnitSection parent)
    {
        super(parent, "println", Arrays.asList("array"));
    }

    @Override
    public Object call(Object... params)
    {
        System.out.println(params[0]);
        return params[0];
    }

}
