package com.github.inc0grepoz.lix4j.ctx;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Represents a non-static pool for variables.
 * 
 * @author inc0g-repoz
 */
public class VarpoolStack implements Cloneable
{

    private static final Object NO_KEY = new Object();

    private final ArrayDeque<Map<String, Object>> stack;

    /**
     * Creates a new non-static pool.
     */
    public VarpoolStack()
    {
        this(new ArrayDeque<>());
    }

    private VarpoolStack(ArrayDeque<Map<String, Object>> stack)
    {
        this.stack = stack;
    }

    @Override
    public String toString()
    {
        StringJoiner joiner = new StringJoiner("\n");

        for (Map<String, Object> layer: stack)
        {
            layer.forEach((k, v) -> joiner.add(k + " = " + v));
        }

        return joiner.toString();
    }

    /**
     * Sets the specified variable in the context section.
     * 
     * @param the variable name
     * @param the variable value
     * @return the variable value
     */
    public Object set(String name, Object value)
    {
        if (stack.isEmpty())
        {
            throw new IllegalStateException("No active section to set variable in");
        }

        for (Map<String, Object> layer: stack)
        {
            if (layer.containsKey(name))
            {
                layer.put(name, value);
                return value;
            }
        }

        stack.peek().put(name, value);
        return value;
    }

    /**
     * Returns the variable by the specified name, if exists.
     * 
     * @param the variable name
     * @return the variable value
     * @throws RuntimeException
     *         if no variable is mapped to the specified name
     */
    public Object get(String name)
    {
        Object o;

        for (Map<String, Object> layer : stack)
        {
            if ((o = layer.getOrDefault(name, NO_KEY)) != NO_KEY)
            {
                return o;
            }
        }

        throw new RuntimeException("Variable '" + name + "' not found in any active section");
    }

    /**
     * Enters a new context section and returns this instance.
     * 
     * @return this instance
     */
    public VarpoolStack enterSection()
    {
        stack.push(new HashMap<>());
        return this;
    }

    /**
     * Exits the current context section and returns this instance.
     * 
     * @return this instance
     */
    public VarpoolStack exitSection()
    {
        if (stack.isEmpty())
        {
            throw new IllegalStateException("No active section to exit");
        }

        stack.pop();
        return this;
    }

    @Override
    public VarpoolStack clone()
    {
        return new VarpoolStack(stack.clone());
    }

}
