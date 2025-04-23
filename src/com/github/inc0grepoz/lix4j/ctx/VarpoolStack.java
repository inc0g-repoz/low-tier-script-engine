package com.github.inc0grepoz.lix4j.ctx;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Represents a context of the execution flow with a
 * pool for variables.
 * 
 * @author inc0g-repoz
 */
public class VarpoolStack implements Cloneable
{

    private static final Object NO_KEY = new Object();

    private final ArrayDeque<Map<String, Object>> layers;

    /**
     * Creates a new non-static pool for the specified
     * {@code script}.
     */
    public VarpoolStack()
    {
        this(new ArrayDeque<>());
    }

    VarpoolStack(ArrayDeque<Map<String, Object>> layers)
    {
        this.layers = layers;
    }

    @Override
    public String toString()
    {
        StringJoiner joiner = new StringJoiner("\n");

        for (Map<String, Object> layer: layers)
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
        if (layers.isEmpty())
        {
            throw new IllegalStateException("No active section to set variable in");
        }

        for (Map<String, Object> layer: layers)
        {
            if (layer.containsKey(name))
            {
                layer.put(name, value);
                return value;
            }
        }

        layers.peek().put(name, value);
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

        for (Map<String, Object> layer : layers)
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
        layers.push(new HashMap<>());
        return this;
    }

    /**
     * Exits the current context section and returns this instance.
     * 
     * @return this instance
     */
    public VarpoolStack exitSection()
    {
        if (layers.isEmpty())
        {
            throw new IllegalStateException("No active section to exit");
        }

        layers.pop();
        return this;
    }

    @Override
    public VarpoolStack clone()
    {
        return new VarpoolStack(layers.clone());
    }

}
