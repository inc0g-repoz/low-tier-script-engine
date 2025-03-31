package com.github.inc0grepoz.ltse.util;

/**
 * A utility class for type-conversion.
 * 
 * @author inc0g-repoz
 */
public class PrimitiveConverter
{

    /**
     * Converts a value to a smaller-sized type
     * without truncating and returns the result.
     * 
     * @param object supposedly a number
     * @return a narrowed value or the same object
     */
    public static Object narrow(Object object)
    {
        if (object instanceof Number)
        {
            Number n  = (Number) object;
            double dv = n.doubleValue();

            if (dv % 1 == 0)
            {
                if (Byte.MIN_VALUE <= dv && dv <= Byte.MAX_VALUE)
                {
                    return n.byteValue();
                }

                if (Short.MIN_VALUE <= dv && dv <= Short.MAX_VALUE)
                {
                    return n.shortValue();
                }

                if (Integer.MIN_VALUE <= dv && dv <= Integer.MAX_VALUE)
                {
                    return n.intValue();
                }

                return n.longValue();
            }

            if (Float.MIN_VALUE < dv && dv <= Float.MAX_VALUE)
            {
                return n.floatValue();
            }
        }

        return object;
    }

    /**
     * Converts numbers into the specified type.
     * 
     * @param object supposedly a number
     * @param type a target type
     * @return a converted value or the same object
     */
    public static Object convert(Object object, Class<?> type)
    {
        if (object instanceof Number)
        {
            Number n  = (Number) object;

            if (type == byte.class || type == Byte.class)
            {
                return n.byteValue();
            }

            if (type == short.class || type == Short.class)
            {
                return n.shortValue();
            }

            if (type == int.class || type == Integer.class)
            {
                return n.intValue();
            }

            if (type == long.class || type == Long.class)
            {
                return n.longValue();
            }

            if (type == float.class || type == Float.class)
            {
                return n.floatValue();
            }

            if (type == double.class || type == Double.class)
            {
                return n.doubleValue();
            }
        }

        return object;
    }

    // Instances of this class should never be made
    private PrimitiveConverter()
    {
        throw new UnsupportedOperationException();
    }

}
