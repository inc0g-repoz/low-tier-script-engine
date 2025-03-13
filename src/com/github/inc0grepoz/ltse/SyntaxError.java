package com.github.inc0grepoz.ltse;

public final class SyntaxError extends RuntimeException
{

    /**
     * Implements serializable.
     */
    private static final long serialVersionUID = -7932907360413850284L;

    public SyntaxError(String message)
    {
        super(message);
    }

    public SyntaxError(String message, Throwable throwable)
    {
        super(message, throwable);
    }

}
