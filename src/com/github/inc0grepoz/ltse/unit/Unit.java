package com.github.inc0grepoz.ltse.unit;

/**
 * Represents a callable unit of scripted logic that can be
 * invoked multiple times
 * 
 * @author inc0g-repoz
 */
public abstract class Unit
{

    final UnitSection parent;

    Unit(UnitSection parent)
    {
        this.parent = parent;

        if (parent != null)
        {
            parent.childs.add(this);
        }
    }

    /**
     * Returns the root unit of the script where this unit
     * has been compiled.
     * 
     * @return the root unit of the script
     */
    UnitRoot root()
    {
        return parent == null ? (UnitRoot) this : parent.root();
    }

    /**
     * Executes this unit with all of it's child units (if
     * it is a section) once and returns an object, if has
     * to, or {@link FlowControl#VOID VOID} otherwise.
     * 
     * @param context the current context of execution
     * @return an object instance
     */
    Object execute(ExecutionContext context)
    {
        throw new UnsupportedOperationException("No " + getClass() + " implementation");
    }

}
