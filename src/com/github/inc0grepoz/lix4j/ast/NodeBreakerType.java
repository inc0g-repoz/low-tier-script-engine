package com.github.inc0grepoz.lix4j.ast;

/**
 * Represents a node breaker type used by token containers
 * for distinguishing blocks from statements and vice versa.
 * 
 * @author inc0g-repoz
 */
public enum NodeBreakerType
{

    /**
     * Indicates that some token container is followed
     * by a block.
     */
    BLOCK,

    /**
     * Indicates that some token container is followed
     * by a statement.
     */
    STATEMENT;

}
