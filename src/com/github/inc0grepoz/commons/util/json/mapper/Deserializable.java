package com.github.inc0grepoz.commons.util.json.mapper;

/**
 * An object to deserialize at some point after deserializing
 * the rest of neighboring instance fields. Can be used, if,
 * for example, some class has various implementations, since
 * JSON format itself doesn't support strict typing.
 * 
 * @author inc0g-repoz
 */
@SuppressWarnings("unchecked")
public class Deserializable
{

    private JsonDeserializer deserializer;
    private Object object;

    private String jsonString;

    Deserializable(JsonDeserializer deserializer, String jsonString)
    {
        this.deserializer = deserializer;
        this.jsonString = jsonString;
    }

    /**
     * Returns a deserialized instance of the class in the argument
     * from it's JSON string representation. The result is cached
     * by this instance.
     * 
     * @param <T> a type of instance that needs to be deserialized
     * @param json a string representation of an instance
     * @param clazz a type of instance that needs to be deserialized
     * @param typeParameters optional type parameters
     * @return a deserialized instance of class
     * @throws JsonException if fails to deserialize an instance
     * @see JsonMapper#deserialize(String, Class, Class...)
     */
    @SafeVarargs
    public final <T> T deserialize(Class<T> clazz, Class<?>... typeParameters)
    throws JsonException
    {
        if (object != null) return (T) object;
        try
        {
            object = deserializer.deserialize(jsonString, clazz, typeParameters);
//          deserializer = null;
//          jsonString = null;
            return (T) object;
        }
        catch (Throwable t)
        {
            throw new JsonException("Failed to deserialize an instance of " + clazz.getName(), t);
        }
    }

    // Returns the JSON String the wrapped instance was deserialized from
    String getJsonString()
    {
        return jsonString;
    }

}
