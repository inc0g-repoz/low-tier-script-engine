package com.github.inc0grepoz.lix4j.unit.expression;

import java.util.LinkedList;
import java.util.regex.Pattern;

import com.github.inc0grepoz.lix4j.Script;
import com.github.inc0grepoz.lix4j.exception.SyntaxError;
import com.github.inc0grepoz.lix4j.unit.UnitSection;
import com.github.inc0grepoz.lix4j.util.TokenHelper;
import com.github.inc0grepoz.lix4j.value.Accessor;
import com.github.inc0grepoz.lix4j.value.AccessorBuilder;
import com.github.inc0grepoz.lix4j.value.AccessorOperator;
import com.github.inc0grepoz.lix4j.value.AccessorValue;

public class ExpressionResolver
{

//  private static final Pattern PATTERN_STRING = Pattern.compile("^\".+\"$");
//  private static final Pattern PATTERN_SPECIAL = Pattern.compile("[~!@#$%^&*()-=+\\[\\]{}:;'\"\\|/<>,.?]+");
    private static final Pattern PATTERN_NUMBER_INT = Pattern.compile("\\d+");
    private static final Pattern PATTERN_NUMBER_LONG = Pattern.compile("(\\d+)[Ll]");
    private static final Pattern PATTERN_NUMBER_FLOAT = Pattern.compile("(\\d*\\.\\d+)[Ff]");
    private static final Pattern PATTERN_NUMBER_DOUBLE = Pattern.compile("(\\d*\\.\\d+)[Dd]?");

    public static Accessor resolve(Script script, UnitSection section, LinkedList<String> tokens)
    {
        TokenHelper.openParentheses(tokens);

        if (tokens.size() == 1)
        {
            return resolveToken(section, tokens.getFirst());
        }

        return resolveOperator(script, section, tokens);
    }

    private static Accessor resolveToken(UnitSection section, String token) {
        if (token == null)
        {
            return Accessor.NULL;
        }

        // Handle reserved tokens
        switch (token)
        {
            case "null":
                return Accessor.NULL;
            case "true":
                return Accessor.TRUE;
            case "false":
                return Accessor.FALSE;
            case "this":
                return Accessor.THIS;
        }

        // Handle string tokens
        if (isStringToken(token))
        {
            String string = TokenHelper.unescape(token.substring(1, token.length() - 1));
            return AccessorValue.of(string);
        }

        // Handle characters
        if (isCharacterToken(token))
        {
            String string = TokenHelper.unescape(token.substring(1, token.length() - 1));

            if (string.length() != 1)
            {
                throw new SyntaxError("Illegal token " + token);
            }

            return AccessorValue.of(string.charAt(0));
        }

        // Handle numeric tokens
        if (PATTERN_NUMBER_INT.matcher(token).matches())
        {
            return AccessorValue.of(Integer.parseInt(token));
        }
        if (PATTERN_NUMBER_LONG.matcher(token).matches())
        {
            return AccessorValue.of(Long.parseLong(token));
        }
        if (PATTERN_NUMBER_FLOAT.matcher(token).matches())
        {
            return AccessorValue.of(Float.parseFloat(token));
        }
        if (PATTERN_NUMBER_DOUBLE.matcher(token).matches())
        {
            return AccessorValue.of(Double.parseDouble(token));
        }

        // Default to variable
        return section.getVarpool().getOrCreate(token);
    }

    private static boolean isStringToken(String token)
    {
        return token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"';
    }

    private static boolean isCharacterToken(String token)
    {
        return token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'';
    }

    private static Accessor resolveOperator(Script script, UnitSection section, LinkedList<String> tokens) {
        for (Operator operator : script.getOperators())
        {
            switch (operator.getType())
            {
            case UNARY_LEFT:
                if (operator.getName().equals(tokens.peekFirst()))
                {
                    tokens.pollFirst();
                    return AccessorOperator.of(operator, resolve(script, section, tokens));
                }
                break;
            case UNARY_RIGHT:
                if (operator.getName().equals(tokens.peekLast()))
                {
                    tokens.pollLast();
                    return AccessorOperator.of(operator, resolve(script, section, tokens));
                }
                break;
            case BINARY:
                if (tokens.contains(operator.getName()))
                {
                    LinkedList<LinkedList<String>> separateTokens = TokenHelper.splitTokens(tokens, operator.getName());
                    if (separateTokens.size() > 1)
                    {
                        Accessor[] operands = new Accessor[separateTokens.size()];
                        for (int i = 0; i < operands.length; i++)
                        {
                            operands[i] = resolve(script, section, separateTokens.poll());
                        }
                        return AccessorOperator.of(operator, operands);
                    }
                }
                break;
            case TERNARY:
                if (tokens.contains("?") && tokens.contains(":"))
                {
                    LinkedList<LinkedList<String>> separateTokens = TokenHelper.splitTokens(tokens, "?", 1);
                    separateTokens.addAll(TokenHelper.splitTokens(separateTokens.pollLast(), ":", 1));
                    if (separateTokens.size() == 3)
                    {
                        Accessor[] operands = new Accessor[separateTokens.size()];
                        for (int i = 0; i < operands.length; i++)
                        {
                            operands[i] = resolve(script, section, separateTokens.poll());
                        }
                        return AccessorOperator.of(operator, operands);
                    }
                }
                break;
            }
        }

        return resolveLinkedAccessor(script, section, tokens);
    }

    private static Accessor resolveLinkedAccessor(Script script, UnitSection section, LinkedList<String> tokens)
    {
        LinkedList<LinkedList<String>> splitTokens = TokenHelper.splitTokens(tokens, ".");
        AccessorBuilder builder = Accessor.builder();
        Accessor index = null;

        while (!splitTokens.isEmpty())
        {
            LinkedList<String> nextTokenList = splitTokens.poll();
            index = resolveIndex(script, section, nextTokenList);

            if (isValueToken(nextTokenList))
            {
                handleValueToken(script, section, builder, nextTokenList, index);
            }
            else if (isParameterizedToken(nextTokenList))
            {
                handleParameterizedToken(script, section, builder, nextTokenList, index);
            }
            else
            {
                handleFieldToken(section, builder, nextTokenList, index);
            }
        }

        return builder.build();
    }

    private static Accessor resolveIndex(Script script, UnitSection section, LinkedList<String> tokens)
    {
        if (tokens.peekLast() != null && tokens.peekLast().equals("]"))
        {
            return resolve(script, section, TokenHelper.readEnclosedTokensBackwards(tokens, "[", "]"));
        }
        return null;
    }

    private static boolean isValueToken(LinkedList<String> tokens)
    {
        return tokens.peekFirst() != null && tokens.peekFirst().equals("(");
    }

    private static boolean isParameterizedToken(LinkedList<String> tokens)
    {
        return tokens.peekLast() != null && tokens.peekLast().equals(")");
    }

    private static void handleValueToken(Script script, UnitSection section, AccessorBuilder builder,
            LinkedList<String> tokens, Accessor index)
    {
        if (builder.isEmpty())
        {
            builder.accessor(resolve(script, section, tokens));
            builder.index(index);
        }
        else
        {
            throw new SyntaxError("Illegal member name: " + String.join(" ", tokens));
        }
    }

    private static void handleParameterizedToken(Script script, UnitSection section, AccessorBuilder builder,
            LinkedList<String> tokens, Accessor index)
    {
        String name = tokens.poll();
        TokenHelper.openParentheses(tokens);

        Accessor[] accessors = tokens.isEmpty()
                ? new Accessor[0]
                : resolveParameters(script, section, TokenHelper.splitTokens(tokens, ","));

        if (builder.isEmpty())
        {
            builder.function(name, accessors);
            builder.index(index);
        }
        else
        {
            builder.method(name, accessors);
            builder.index(index);
        }
    }

    private static Accessor[] resolveParameters(Script script, UnitSection section, LinkedList<LinkedList<String>> paramList)
    {
        Accessor[] accessors = new Accessor[paramList.size()];

        for (int i = 0; i < accessors.length; i++)
        {
            accessors[i] = resolve(script, section, paramList.poll());
        }

        return accessors;
    }

    private static void handleFieldToken(UnitSection section, AccessorBuilder builder, LinkedList<String> tokens, Accessor index)
    {
        if (tokens.size() > 1)
        {
            throw new SyntaxError("Unresolved expression: " + String.join(" ", tokens));
        }

        if (builder.isEmpty())
        {
            builder.accessor(resolveToken(section, tokens.poll()));
            builder.index(index);
        }
        else
        {
            builder.field(tokens.poll());
            builder.index(index);
        }
    }

    private ExpressionResolver()
    {
        throw new UnsupportedOperationException("Utility class");
    }

}
