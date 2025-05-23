package com.github.inc0grepoz.lix4j.unit.inbuilt;

import java.util.Arrays;

import com.github.inc0grepoz.lix4j.unit.UnitFunction;
import com.github.inc0grepoz.lix4j.unit.UnitSection;

public class InBuiltPrintln extends UnitFunction
{

    public InBuiltPrintln(UnitSection parent)
    {
        super(parent, "println", Arrays.asList("object"));
    }

    @Override
    public Object call(Object... params)
    {
        System.out.println(params[0]);
        return params[0];
    }

}
