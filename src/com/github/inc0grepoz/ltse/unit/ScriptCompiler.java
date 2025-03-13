package com.github.inc0grepoz.ltse.unit;

import java.util.LinkedList;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.SyntaxError;
import com.github.inc0grepoz.ltse.ast.AST;
import com.github.inc0grepoz.ltse.ast.ASTNode;
import com.github.inc0grepoz.ltse.ast.NodeBreakerType;

public class ScriptCompiler
{

    public static UnitRoot compile(Script script, AST ast)
    {
        UnitRoot root = new UnitRoot();
        compileSection_r(script, ast, root);
        return root;
    }

    static Unit compileUnit_r(Script script, ASTNode node, UnitSection parent)
    {
        if (!node.hasTokens())
        {
            return compileSection_r(script, node, new UnitSection(parent));
        }

        Unit unit;

        switch (node.getTokens().peek())
        {
        case "else":
            if (parent.childs.getLast() instanceof UnitConditionalIf)
            {
                UnitConditionalIf cif = (UnitConditionalIf) parent.childs.getLast();
                unit = cif.otherwise = UnitConditionalElse.compile(script, node, null);
            }
            else
            {
                String breaker = node.getNodeBreakerType().name().toLowerCase();
                throw new SyntaxError("Unexpected else " + breaker);
            }
            break;
//      case "for":
        case "function":
            unit = UnitFunction.compile(script, node, parent);
            break;
        case "if":
            unit = UnitConditionalIf.compile(script, node, parent);
            break;
        case "return":
            unit = UnitReturn.compile(script, node, parent);
            break;
        case "while":
            unit = UnitConditionalWhile.compile(script, node, parent);
            break;
        default:
            unit = UnitOperation.compile(script, node, parent);
        }

        if (node.getNodeBreakerType() == NodeBreakerType.BLOCK)
        {
            if (unit instanceof UnitSection)
            {
                ASTNode next = node.getParent().getChildNodes().poll();
                compileSection_r(script, next, (UnitSection) unit);
            }
            else
            {
                throw new IllegalStateException("Attempted to extend a statement with a block of code");
            }
        }

        return unit;
    }

    private static UnitSection compileSection_r(Script script, ASTNode node, UnitSection parent)
    {
        LinkedList<ASTNode> childs = node.getChildNodes();

        while (!childs.isEmpty())
        {
            compileUnit_r(script, childs.poll(), parent);
        }

        return parent;
    }

}
