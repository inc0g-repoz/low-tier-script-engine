package com.github.inc0grepoz.ltse.unit.expression;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Pattern;

import com.github.inc0grepoz.ltse.Script;
import com.github.inc0grepoz.ltse.SyntaxError;
import com.github.inc0grepoz.ltse.value.Accessor;
import com.github.inc0grepoz.ltse.value.AccessorBuilder;
import com.github.inc0grepoz.ltse.value.AccessorOperator;
import com.github.inc0grepoz.ltse.value.AccessorValue;
import com.github.inc0grepoz.ltse.value.AccessorVariable;

public class ExpressionResolver
{

    static final Accessor ACCESSOR_NULL = AccessorValue.of(null);
    static final Accessor ACCESSOR_TRUE = AccessorValue.of(true);
    static final Accessor ACCESSOR_FALSE = AccessorValue.of(false);

    static final Pattern PATTERN_STRING = Pattern.compile("^\".+\"$");
    static final Pattern PATTERN_SPECIAL = Pattern.compile("[~!@#$%^&*()-=+\\[\\]{}:;'\"\\|/<>,.?]+");
    static final Pattern PATTERN_NUMBER_INT = Pattern.compile("\\d+");
    static final Pattern PATTERN_NUMBER_LONG = Pattern.compile("\\d+[Ll]");
    static final Pattern PATTERN_NUMBER_FLOAT = Pattern.compile("\\d*\\.\\d+[Ff]");
    static final Pattern PATTERN_NUMBER_DOUBLE = Pattern.compile("\\d*\\.\\d+[Dd]?");

    public static LinkedList<LinkedList<String>> splitTokens(LinkedList<String> tokens, String separator)
    {
        int parentheses = 0;

        LinkedList<LinkedList<String>> separateTokens = new LinkedList<>();

        ListIterator<String> iter = tokens.listIterator();
        LinkedList<String> tokenList = new LinkedList<>();
        String next;

        while (iter.hasNext())
        {
            switch (next = iter.next())
            {
            case "(":
                parentheses++;
                break;
            case ")":
                parentheses--;
                break;
            }

            if (parentheses == 0 && next.equals(separator))
            {
                // Flushing the previous tokens
                separateTokens.add(tokenList);
                tokenList = new LinkedList<>();
            }
            else
            {
                // Writing the observed token
                tokenList.add(next);
            }
        }

        // Flushing the last sequence
        separateTokens.add(tokenList);

        return separateTokens;
    }

    public static LinkedList<String> openParentheses(LinkedList<String> tokens)
    {
        while (tokens.size() > 1
                && tokens.getFirst().equals("(")
                && tokens.getLast().equals(")"))
        {
            tokens.removeFirst();
            tokens.removeLast();
        }
        return tokens;
    }

    public static Accessor resolve(Script script, LinkedList<String> tokens)
    {
        openParentheses(tokens);

        if (tokens.size() == 1)
        {
            return resolveToken(tokens.getFirst());
        }

//      throw new RuntimeException("Unresolved expression: " + String.join(" ", tokens));
        return resolveOperator(script, tokens);
    }

    private static Accessor resolveToken(String token)
    {
        // Persistent (reserved tokens)
        switch (token)
        {
        case "null":
            return ACCESSOR_NULL;
        case "true":
            return ACCESSOR_TRUE;
        case "false":
            return ACCESSOR_FALSE;
        }

        // String tokens
        if (token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"')
        {
            return AccessorValue.of(token.substring(1, token.length() - 1));
        }

        // Integer tokens
        if (PATTERN_NUMBER_INT.matcher(token).matches())
        {
            return AccessorValue.of(Integer.parseInt(token));
        }

        // Long tokens
        if (PATTERN_NUMBER_LONG.matcher(token).matches())
        {
            return AccessorValue.of(Long.parseLong(token));
        }

        // Float tokens
        if (PATTERN_NUMBER_FLOAT.matcher(token).matches())
        {
            return AccessorValue.of(Float.parseFloat(token));
        }

        // Double tokens
        if (PATTERN_NUMBER_DOUBLE.matcher(token).matches())
        {
            return AccessorValue.of(Double.parseDouble(token));
        }

        // Variable
        return AccessorVariable.of(token);
    }

    private static Accessor resolveOperator(Script script, LinkedList<String> tokens)
    {
        LinkedList<LinkedList<String>> separateTokens;

        for (Operator operator: script.getOperators())
        {
            separateTokens = splitTokens(tokens, operator.getName());

            if (separateTokens.size() > 1)
            {
                Accessor[] operands = new Accessor[separateTokens.size()];

                for (int i = 0; i < operands.length; i++)
                {
                    operands[i] = resolve(script, separateTokens.poll());
                }

                return AccessorOperator.of(operator, operands);
            }
        }

        return resolveLinkedAccessor(script, tokens);
    }

    private static Accessor resolveLinkedAccessor(Script script, LinkedList<String> tokens)
    {
        LinkedList<LinkedList<String>> splitTokens = splitTokens(tokens, ".");
        LinkedList<String> nextTokenList;
        AccessorBuilder builder = Accessor.builder();

        while (!splitTokens.isEmpty())
        {
            nextTokenList = splitTokens.poll();

            if (nextTokenList.getFirst().equals("(")) // value
            {
                if (builder.isEmpty())
                {
                    builder.accessor(resolve(script, nextTokenList));
                }
                else
                {
                    throw new SyntaxError("Illegal member name: " + String.join(" ", nextTokenList));
                }
            }
            else if (nextTokenList.getLast().equals(")")) // parameterized accessor
            {
                String name = nextTokenList.poll();
                openParentheses(nextTokenList);

                Accessor[] accessors;
                if (nextTokenList.isEmpty())
                {
                    accessors = new Accessor[0];
                }
                else
                {
                    LinkedList<LinkedList<String>> paramList = splitTokens(nextTokenList, ",");
                    accessors = new Accessor[paramList.size()];

                    for (int i = 0; i < accessors.length; i++)
                    {
                        accessors[i] = resolve(script, paramList.poll());
                    }
                }

                if (builder.isEmpty()) // in the beginning
                {
                    builder.function(name, accessors);
                }
                else // somewhere later
                {
                    builder.method(name, accessors);
                }
            }
            else // field
            {
                if (builder.isEmpty())
                {
                    builder.accessor(resolveToken(nextTokenList.poll()));
                }
                else
                {
                    builder.field(nextTokenList.poll());
                }
            }
        }

//      System.out.println("Linked Accessor: " + builder);
//      System.out.println("Length: " + builder.length());
//      System.out.println("Type: " + builder.build().getClass());
        return builder.build();
    }

}
