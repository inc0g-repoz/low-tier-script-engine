package com.github.inc0grepoz.lix4j.unit;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.ast.ASTNode;
import com.github.inc0grepoz.lix4j.util.FlowControl;

public class UnitFunction extends UnitSection
{

    static final Object[] EMPTY_ARRAY = new Object[0];

    static UnitFunction compile(Script script, ASTNode node, UnitSection section)
    {
        LinkedList<String> tokens = node.getTokens();

        tokens.poll(); // function
        String name = tokens.poll();
        String token = tokens.poll();

        if (!token.equals("("))
        {
            throw new IllegalStateException("'(' expected, but \"" + token + "\" found");
        }

        List<String> paramNames;

        if ((token = tokens.poll()).equals(")"))
        {
            paramNames = Collections.emptyList();
        }
        else
        {
            (paramNames = new ArrayList<>()).add(token);

            while ((token = tokens.poll()).equals(","))
            {
                paramNames.add(tokens.poll());
            }

            if (!token.equals(")"))
            {
                throw new IllegalStateException("')' expected, but \"" + token + "\" found");
            }
        }

        UnitFunction fn = script.getFunction(name, paramNames.size());

        if (fn != null)
        {
            throw new RuntimeException("Function overloading is not supported (duplicate function " + fn.getSignature() + ")");
        }

        fn = new UnitFunction(section, name, paramNames);
        ScriptCompiler.appendSectionUnits(script, node, fn);

        return fn;
    }

    final String name;
    final List<String> paramNames;
    final UnitRoot root;

    protected UnitFunction(UnitSection parent, String name, List<String> paramNames)
    {
        super(parent);
        this.name = name;
        this.paramNames = paramNames;
        root = root();
    }

    @Override
    public String toString()
    {
        return "function " + getSignature() + " " + super.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof UnitFunction))
        {
            return false;
        }

        UnitFunction fn = (UnitFunction) obj;

        return fn.paramNames.size() == paramNames.size()
                && fn.name.equals(name);
    }

    @Override
    Object execute(ExecutionContext context)
    {
        return FlowControl.KEEP_EXECUTING;
    }

    /**
     * Calls this function and returns an object, if has to,
     * or {@link FlowControl#VOID VOID} otherwise.
     * 
     * @param params the function parameters
     * @return an object instance
     */
    public Object call(Object... params)
    {
        if (params == null)
        {
            params = EMPTY_ARRAY;
        }

        if (params.length != paramNames.size())
        {
            String desc = name + "(" + String.join(", ", paramNames) + ")";
            throw new IllegalArgumentException(desc);
        }

        ExecutionContext context = root.getScript().supplyContext();

        for (int i = 0; i < params.length; i++)
        {
            varpool.getOrCreate(paramNames.get(i)).mutate(context, null, params[i]);
        }

        Object rv = super.execute(context);

        return rv == FlowControl.KEEP_EXECUTING ? FlowControl.VOID : rv;
    }

    /**
     * Returns a string representation of the signature.
     * 
     * @return a string representation of the signature
     */
    public String getSignature()
    {
        return name + "(" + String.join(", ", paramNames) + ")";
    }

    /**
     * Returns a {@code List} of parameter names in the
     * function definition.
     * 
     * @return a {@code List} of parameter names
     */
    public List<String> getParameterNames()
    {
        return paramNames;
    }

    /**
     * Implements a functional interface using a proxy instance
     * and returns it.
     * 
     * @param type the functional interface class to implement
     * @return a proxy instance
     */
    public Object createProxy(Class<?> type)
    {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type },
                (proxy, method, args) -> call(args));
    }

}
