package com.github.inc0grepoz.ltse.ast;

import java.util.LinkedList;

import com.github.inc0grepoz.ltse.SyntaxError;

public class AST extends ASTNodeSection
{

    public static AST generateTree(LinkedList<String> input)
    {
        AST root = new AST();
        ASTNodeSection curSection = root;
        ASTNodeTokens curTokens = null;

        String nextTokenString;
        int braces = 0;

        while (!input.isEmpty())
        {
            nextTokenString = input.poll();

            switch (nextTokenString)
            {
            case "(":
                if (curTokens == null)
                {
                    curTokens = new ASTNodeTokens(curSection);
                }
                curTokens.getTokens().add(nextTokenString);
                braces++;
                break;
            case ")":
                if (curTokens == null)
                {
                    curTokens = new ASTNodeTokens(curSection);
                }
                curTokens.getTokens().add(nextTokenString);
                braces--;
                break;
            case "{":
                if (curTokens != null)
                {
                    curTokens.setNodeBreakerType(NodeBreakerType.BLOCK);
                }
                curSection = new ASTNodeSection(curSection);
                curTokens = null; // entering a new section
                break;
            case "}":
                curSection = curSection.getParent();
                curTokens = null; // exiting a section
                break;
            case ";":
                if (braces == 0)
                {
                    curTokens = null; // ending a statement
                }
                else
                {
                    if (curTokens == null)
                    {
                        curTokens = new ASTNodeTokens(curSection);
                    }
                    curTokens.getTokens().add(nextTokenString);
                }
                break;
            default:
                if (curTokens == null)
                {
                    curTokens = new ASTNodeTokens(curSection);
                }
                curTokens.getTokens().add(nextTokenString);
            }
        }

        if (root != curSection)
        {
            throw new SyntaxError("Unterminated curly braces");
        }

        if (braces != 0)
        {
            throw new SyntaxError("Unterminated parentheses");
        }

        return root;
    }

    private AST()
    {
        super(null);
    }

}
