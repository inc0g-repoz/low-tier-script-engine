package com.github.inc0grepoz.lix4j.util;

import java.util.LinkedList;
import java.util.ListIterator;

import com.github.inc0grepoz.lix4j.exception.SyntaxError;

public class TokenHelper
{

    public static LinkedList<String> readEnclosedTokens(LinkedList<String> tokens,
            String prefix, String suffix)
    {
        LinkedList<String> out = new LinkedList<>();
        String token = tokens.poll();
        int level = 1;

        if (!prefix.equals(token))
        {
            throw new SyntaxError("\"" + prefix + "\" expected, but \"" + token + "\" found");
        }

        while (true)
        {
            token = tokens.poll();

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

            out.add(token);
        }

        return out;
    }

    public static LinkedList<String> readEnclosedTokensBackwards(LinkedList<String> tokens,
            String prefix, String suffix)
    {
        LinkedList<String> out = new LinkedList<>();
        String token = tokens.pollLast();
        int level = 1;

        if (!suffix.equals(token))
        {
            throw new SyntaxError("\"" + suffix + "\" expected, but \"" + token + "\" found");
        }

        while (true)
        {
            token = tokens.pollLast();

            if (token == null)
            {
                throw new SyntaxError("Unterminated sequence in " + prefix + suffix);
            }
            else if (suffix.equals(token))
            {
                level++;
            }
            else if (prefix.equals(token))
            {
                level--;
            }

            if (level == 0)
            {
                break;
            }

            out.addFirst(token);
        }

        return out;
    }

    public static LinkedList<LinkedList<String>> splitTokens(LinkedList<String> tokens, String separator, int limit)
    {
        int parentheses = 0;
        int squareBrackets = 0;

        LinkedList<LinkedList<String>> separateTokens = new LinkedList<>();

        ListIterator<String> iter = tokens.listIterator();
        LinkedList<String> tokenList = new LinkedList<>();
        String next;

        while (iter.hasNext())
        {
            switch (next = iter.next())
            {
            case "[":
                squareBrackets++;
                break;
            case "]":
                squareBrackets--;
                break;
            case "(":
                parentheses++;
                break;
            case ")":
                parentheses--;
                break;
            }

            if (0 < limit
                    && squareBrackets == 0
                    && parentheses    == 0
                    && next.equals(separator))
            {
                limit--;

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

    public static LinkedList<LinkedList<String>> splitTokens(LinkedList<String> tokens, String separator)
    {
        return splitTokens(tokens, separator, Integer.MAX_VALUE);
    }

    public static LinkedList<String> openParentheses(LinkedList<String> tokens)
    {
        int balance;

        while (tokens.size() > 1
                && tokens.getFirst().equals("(")
                && tokens.getLast().equals(")"))
        {
            tokens.removeFirst();
            tokens.removeLast();
            balance = 0;

            for (String token: tokens)
            {
                switch (token)
                {
                case "(":
                    balance++;
                    break;
                case ")":
                    balance--;
                    break;
                }

                if (balance < 0)
                {
                    tokens.addFirst("(");
                    tokens.addLast(")");

                    return tokens;
                }
            }
        }

        return tokens;
    }

    public static String unescape(String string)
    {
        StringBuilder builder = new StringBuilder(string.length());

        for (int i = 0; i < string.length(); i++)
        {
            char ch = string.charAt(i);
            if (ch == '\\' && i + 1 < string.length())
            {
                switch (string.charAt(i + 1))
                {
                case '\\':
                    builder.append('\\');
                    i++;
                    break;
                case '\"':
                    builder.append('"');
                    i++;
                    break;
                case '\'':
                    builder.append('\'');
                    i++;
                    break;
                case 't':
                    builder.append('\t');
                    i++;
                    break;
                case 'b':
                    builder.append('\b');
                    i++;
                    break;
                case 'n':
                    builder.append('\n');
                    i++;
                    break;
                case 'r':
                    builder.append('\r');
                    i++;
                    break;
                case 'f':
                    builder.append('\f');
                    i++;
                    break;
                case 'u': // Unicode escape
                    if (i + 5 < string.length())
                    {
                        String hex = string.substring(i + 2, i + 6);
                        builder.append((char) Integer.parseInt(hex, 16));
                        i += 5;
                    }
                    break;
                default:
                    builder.append(ch);
                }
            }
            else
            {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    private TokenHelper()
    {
        throw new UnsupportedOperationException("Utility class");
    }

}
