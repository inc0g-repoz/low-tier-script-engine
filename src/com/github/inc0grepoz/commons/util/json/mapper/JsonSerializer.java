package com.github.inc0grepoz.commons.util.json.mapper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.UUID;

import com.github.inc0grepoz.commons.util.json.mapper.annotation.IgnoreEmpty;

/**
 * Serializes Java instances into JSON string values.
 * 
 * @author inc0g-repoz
 */
public class JsonSerializer
{

    // Not designed to be instantiated outside the mapper
    JsonSerializer() {}

    String serialize(Object instance)
    throws Throwable
    {
        if (instance == null)
        {
            return null;
        }
        else if (PrimitiveTester.isPrimitiveType(instance))
        {
            return PrimitiveTester.isDefaultValue(instance) ? null : String.valueOf(instance);
        }
        else if (instance.getClass() == String.class)
        {
            return "\"" + escapeQuotes((String) instance) + "\"";
        }
        else if (instance.getClass() == Deserializable.class)
        {
            return ((Deserializable) instance).getJsonString();
        }
        else if (instance.getClass().isEnum())
        {
            return "\"" + ((Enum<?>) instance).name() + "\"";
        }
        else if (instance.getClass() == UUID.class)
        {
            return "\"" + instance.toString() + "\"";
        }
        else if (instance.getClass().isArray())
        {
            // Creating a string joiner to write all values
            // of the array and delimit them by a comma
            StringJoiner arrayJoiner = new StringJoiner(", ", "[", "]");

            // Iterating through all the array elements
            for (int i = 0; i < Array.getLength(instance); i++)
            {
                arrayJoiner.add(serialize(Array.get(instance, i)));
            }

            // Joining the array collection into the compound
            return arrayJoiner.toString();
        }
        else if (instance instanceof Collection)
        {
            // Creating a string joiner to write all values
            // of the collection and delimit them by a comma
            StringJoiner collectionJoiner = new StringJoiner(", ", "[", "]");

            // Iterating through all the collection elements
            for (Object element: (Iterable<?>) instance)
            {
                collectionJoiner.add(serialize(element));
            }

            // Joining the serialized collection into the compound
            return collectionJoiner.toString();
        }
        else if (instance instanceof Map)
        {
            // Creating a string joiner to write all values
            // of the map and delimit them by a comma
            StringJoiner mapJoiner = new StringJoiner(", ", "{", "}");

            // Iterating through all the key value pairs
            Map<?, ?> map = (Map<?, ?>) instance;
            for (Entry<?, ?> entry: map.entrySet())
            {
                mapJoiner.add(serialize(entry.getKey()) + ": " + serialize(entry.getValue()));
            }

            // Joining the serialized map into the compound
            return mapJoiner.toString();
        }

        // Creating a string joiner to write each field
        // and it's value in a string representation
        StringJoiner compoundJoiner = new StringJoiner(", ", "{", "}");

        // Creating a class reference to walk through all fields
        // declared in the passed object including ones inherited
        // from it's superclasses
        Class<?> clazz = instance.getClass();

        // Temporary object to a field value pointer
        Object temp;

        // Serializing fields values for each inherited class
        // Breaking the loop if no more inherited classes found
        while (clazz != null)
        {

            // Walking through all fields of the object class
            // or it's super class if present
            for (Field field: clazz.getDeclaredFields())
            {
                if (Modifier.isFinal(field.getModifiers())  ||
                    Modifier.isStatic(field.getModifiers()) ||
                    Modifier.isTransient(field.getModifiers()))
                {
                    continue; // Skipping, if it's transient
                }

                // Retrieving the field value
                field.setAccessible(true);
                temp = field.get(instance);

                // Only write non-null values
                if (!PrimitiveTester.isDefaultValue(temp))
                {
                    if (field.isAnnotationPresent(IgnoreEmpty.class)
                            && (boolean) temp.getClass().getMethod("isEmpty").invoke(temp))
                    {
                        continue; // Skipping, if it's an empty collection (map)
                    }

                    compoundJoiner.add("\"" + field.getName() + "\": " + serialize(temp));
                }
            }

            clazz = clazz.getSuperclass();
        }

        return compoundJoiner.toString();
    }

    // Creates a copy of String, where every '"' quote
    // is escaped (followed by a '\')
    private String escapeQuotes(String string)
    {
        int sz = string.length();
        char ch;

        StringBuilder sb = new StringBuilder(sz);

        for (int i = 0; i < sz; i++)
        {
            ch = string.charAt(i);
            if (ch == '"')
            {
                sb.append('\\');
                sb.append('"');
            }
            else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

}
