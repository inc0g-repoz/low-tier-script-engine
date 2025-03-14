package com.github.inc0grepoz.ltse.unit;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import com.github.inc0grepoz.ltse.Script;

public class ExecutionContext
{

    private static final Object NO_KEY = new Object();

    private final Script script;
    private final Deque<Map<String, Object>> layers = new ArrayDeque<>();

    public ExecutionContext(Script script)
    {
        this.script = script;
    }

    public Script getScript()
    {
        return script;
    }

    public Object setVariable(String name, Object value)
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

    public Object getVariable(String name)
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

    ExecutionContext enterSection()
    {
        layers.push(new HashMap<>());
        return this;
    }

    ExecutionContext exitSection()
    {
        if (layers.isEmpty())
        {
            throw new IllegalStateException("No active section to exit");
        }

        layers.pop();
        return this;
    }

}
