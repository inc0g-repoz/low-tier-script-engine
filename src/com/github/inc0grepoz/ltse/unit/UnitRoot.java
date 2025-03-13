package com.github.inc0grepoz.ltse.unit;

public class UnitRoot extends UnitSection
{

    UnitRoot()
    {
        super(null);
    }

    public Object callFunction(String name, Object... args)
    {
        UnitFunction fn;

        for (Unit child: childs)
        {
            if (child instanceof UnitFunction)
            {
                fn = ((UnitFunction) child);

                if (fn.name.equals(name))
                {
                    if (fn.paramNames.size() != args.length)
                    {
                        throw new IllegalArgumentException(fn.name + "(" + String.join(", ", fn.paramNames) + ")");
                    }

                    ExecutionContext context = new ExecutionContext();
                    context.enterSection();

                    for (int i = 0; i < args.length; i++)
                    {
                        context.setVariable(fn.paramNames.get(i), args[i]);
                    }

                    return child.execute(context);
                }
            }
        }

        return null;
    }

}
