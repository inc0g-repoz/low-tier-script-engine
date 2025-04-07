package com.github.inc0grepoz.lix4j.value;

abstract class AccessorNamed extends Accessor
{

    protected final String name;

    AccessorNamed(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}
