package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.ltse.Script;

public class UnitSection extends Unit
{

    final LinkedList<Unit> childs = new LinkedList<>();

    UnitSection(UnitSection parent)
    {
        super(parent);
    }

    @Override
    Object execute(ExecutionContext context)
    {
        Object rv;
        context.enterSection();

        for (Unit unit: childs)
        {
            rv = unit.execute(context);

            if (rv != Script.KEEP_EXECUTING)
            {
                return rv;
            }
        }

        context.exitSection();
        return Script.KEEP_EXECUTING;
    }

}
