package com.github.inc0grepoz.lix4j.ctx;

import com.github.inc0grepoz.lix4j.Script;

/**
 * Represents a context of the execution flow with a
 * pool for variables.
 * 
 * @author inc0g-repoz
 */
public class ExecutionContext implements Cloneable
{

    private final Script script;
    private final VarpoolStack varpool;

    /**
     * Creates a new global context for the specified {@code script}.
     * 
     * @param script the {@code Script}
     */
    public ExecutionContext(Script script)
    {
        this(script, new VarpoolStack());
    }

    private ExecutionContext(Script script, VarpoolStack varpool)
    {
        this.script = script;
        this.varpool = varpool;
    }

    @Override
    public String toString()
    {
        return script.toString();
    }

    /**
     * Returns the {@code Script} that supplied this {@code ExecutionContext}.
     * 
     * @return the {@code Script} that supplied this {@code ExecutionContext}
     */
    public Script getScript()
    {
        return script;
    }

    public VarpoolStack getVarpool()
    {
        return varpool;
    }

    @Override
    public ExecutionContext clone()
    {
        return new ExecutionContext(script);
    }

}
