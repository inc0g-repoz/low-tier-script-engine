package com.github.inc0grepoz.ltse.ast;

import java.util.LinkedList;

public abstract class ASTNode
{

    final ASTNodeSection parent;

    ASTNode(ASTNodeSection parent)
    {
        this.parent = parent;

        if (parent != null)
        {
            parent.childs.add(this);
        }
    }

    public ASTNodeSection getParent()
    {
        return parent;
    }

    public LinkedList<ASTNode> getChildNodes()
    {
        throw new UnsupportedOperationException();
    }

    public LinkedList<String> getTokens()
    {
        throw new UnsupportedOperationException();
    }

    public LinkedList<String> readEnclosedTokens(String prefix, String suffix)
    {
        throw new UnsupportedOperationException();
    }

    public NodeBreakerType getNodeBreakerType()
    {
        throw new UnsupportedOperationException();
    }

    public abstract boolean hasChildNodes();

    public abstract boolean hasTokens();

    int indentation()
    {
        ASTNode node = parent;

        int i = 0;
        for (; node != null; i++)
        {
            node = node.parent;
        }

        return i;
    }

}
