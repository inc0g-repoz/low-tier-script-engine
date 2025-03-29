package com.github.inc0grepoz.ltse.unit.inbuilt;

import java.util.Arrays;

import com.github.inc0grepoz.ltse.unit.UnitFunction;
import com.github.inc0grepoz.ltse.unit.UnitSection;
import com.github.inc0grepoz.ltse.value.AccessorNoInstance;

public class InBuiltNoInstance extends UnitFunction
{

    public InBuiltNoInstance(UnitSection parent)
    {
        super(parent, "no_instance", Arrays.asList("class"));
    }

    @Override
    public Object call(Object... params)
    {
        try {
            return AccessorNoInstance.of(Class.forName((String) params[0]));
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

}
