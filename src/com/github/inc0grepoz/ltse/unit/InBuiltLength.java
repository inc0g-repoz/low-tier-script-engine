package com.github.inc0grepoz.ltse.unit;

import java.lang.reflect.Array;
import java.util.Arrays;

public class InBuiltLength extends UnitFunction
{

    InBuiltLength(UnitSection parent)
    {
        super(parent, "length", Arrays.asList("array"));
    }

    @Override
    public Object call(Object... params)
    {
        return Array.getLength(params[0]);
    }

}
