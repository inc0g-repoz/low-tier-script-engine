package com.github.inc0grepoz.ltse.ast;

import java.util.LinkedList;
import java.util.stream.Collectors;

import com.github.inc0grepoz.ltse.SyntaxError;

public class ASTNodeTokens extends ASTNode
{

    protected final LinkedList<String> tokens = new LinkedList<>();

    protected NodeBreakerType breaker = NodeBreakerType.STATEMENT;

    ASTNodeTokens(ASTNodeSection parent)
    {
        super(parent);
    }

    @Override
    public String toString()
    {
        String indentation = new String(new char[indentation()]).replace("\0", "    ");
        return indentation + tokens.stream().collect(Collectors.joining(" ")) + " " + breaker;
    }

    @Override
    public LinkedList<String> getTokens()
    {
        return tokens;
    }

    @Override
    public LinkedList<String> readEnclosedTokens(String prefix, String suffix)
    {
        LinkedList<String> tokens = new LinkedList<>();
        String token = this.tokens.poll();
        int level = 1;

        if (!prefix.equals(token))
        {
            throw new SyntaxError("\"" + prefix + "\" expected, but \"" + token + "\" found");
        }

        while (true)
        {
            token = this.tokens.poll();

            if (token == null)
            {
                throw new SyntaxError("Unterminated sequence in " + prefix + suffix);
            }
            else if (prefix.equals(token))
            {
                level++;
            }
            else if (suffix.equals(token))
            {
                level--;
            }

            if (level == 0)
            {
                break;
            }

            tokens.add(token);
        }

        return tokens;
    }

    @Override
    public NodeBreakerType getNodeBreakerType()
    {
        return breaker;
    }

    public void setNodeBreakerType(NodeBreakerType breaker)
    {
        this.breaker = breaker;
    }

    @Override
    public boolean hasChildNodes()
    {
        return false;
    }

    @Override
    public boolean hasTokens()
    {
        return true;
    }

}
