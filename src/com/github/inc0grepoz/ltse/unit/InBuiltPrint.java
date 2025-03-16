package com.github.inc0grepoz.ltse.unit;

import java.util.Arrays;

public class InBuiltPrint extends UnitFunction
{

    InBuiltPrint(UnitSection parent)
    {
        super(parent, "print", Arrays.asList("array"));
    }

    @Override
    public Object call(Object... params)
    {
        System.out.print(params[0]);
        return params[0];
    }

}
