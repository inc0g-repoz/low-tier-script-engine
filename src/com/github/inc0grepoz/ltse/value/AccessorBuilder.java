package com.github.inc0grepoz.ltse.value;

public class AccessorBuilder
{

    private Accessor first, last;
    private int length;

    AccessorBuilder() {}

    @Override
    public String toString()
    {
        return first == null ? null : first.toString();
    }

    public AccessorBuilder accessor(Accessor accessor)
    {
        length++;

        if (first == null)
        {
            first = last = accessor;
        }
        else
        {
            last = last.next = accessor;
        }

        return this;
    }

    public AccessorBuilder field(String name)
    {
        return accessor(new AccessorField(name));
    }

    public AccessorBuilder method(String name, Accessor... params)
    {
        return accessor(new AccessorMethod(name, params));
    }

    public AccessorBuilder function(String name, Accessor... params)
    {
        return accessor(new AccessorFunction(name, params));
    }

    public AccessorBuilder index(Accessor accessor)
    {
        last.elementIndex = accessor;
        return this;
    }

    public Accessor build()
    {
        return first;
    }

    public int length()
    {
        return length;
    }

    public boolean isEmpty()
    {
        return length == 0;
    }

}
