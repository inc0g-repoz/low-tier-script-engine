package com.github.inc0grepoz.ltse.unit;

import java.lang.reflect.Array;
import java.util.Arrays;

public class InBuiltNewArray extends UnitFunction
{

    InBuiltNewArray(UnitSection parent)
    {
        super(parent, "new_array", Arrays.asList("class", "size"));
    }

    @Override
    public Object call(Object... params)
    {
        try {
            Class<?> clazz = Class.forName((String) params[0]);
            int size = (int) params[1];
            return Array.newInstance(clazz, size);
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

}
