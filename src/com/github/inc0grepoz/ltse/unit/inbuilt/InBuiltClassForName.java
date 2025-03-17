package com.github.inc0grepoz.ltse.unit.inbuilt;

import java.util.Arrays;

import com.github.inc0grepoz.ltse.unit.UnitFunction;
import com.github.inc0grepoz.ltse.unit.UnitSection;

public class InBuiltClassForName extends UnitFunction
{

    public InBuiltClassForName(UnitSection parent)
    {
        super(parent, "class_for_name", Arrays.asList("class"));
    }

    @Override
    public Object call(Object... params)
    {
        try {
            return Class.forName((String) params[0]);
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

}
