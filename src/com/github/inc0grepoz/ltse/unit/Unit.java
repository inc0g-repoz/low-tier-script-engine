package com.github.inc0grepoz.ltse.unit;

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

    Object execute(ExecutionContext context)
    {
        throw new UnsupportedOperationException("No " + getClass() + " implementation");
    }

}
