package com.github.inc0grepoz.ltse;

public enum FlowControl
{

    /**
     * Used by script units to continue executing,
     * if one of the units returns a value.
     */
    KEEP_EXECUTING,

    /**
     * Used as a function return value, when no value
     * is returned.
     */
    VOID;

}
