package com.github.inc0grepoz.commons.util.json.mapper;

@SuppressWarnings("serial")
public class JsonException extends RuntimeException
{

    JsonException(String message)
    {
        super(message);
    }

    JsonException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

}
