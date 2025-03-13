package com.github.inc0grepoz.util;

/**
 * Static convenience methods that help a method or constructor check whether it was invoked
 * correctly (that is, whether its <i>preconditions</i> were met).
 *
 * <p>If the precondition is not met, the {@code Preconditions} method throws an unchecked exception
 * of a specified type, which helps the method in which the exception was thrown communicate that
 * its caller has made a mistake.
 *
 * @author inc0g-repoz
 */
public final class Preconditions
{

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression)
    {
        if (!expression) throw new IllegalArgumentException();
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression, Object errorMessage)
    {
        if (!expression) throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference)
    {
        if (reference == null) throw new NullPointerException();
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, Object errorMessage)
    {
        if (reference == null) throw new NullPointerException(String.valueOf(errorMessage));
        return reference;
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size)
    {
        if (index < 0) throw new IllegalArgumentException
            ("index (" + index + ") must not be negative");
        if (size <= 0) throw new IllegalArgumentException
            ("size (" + size + ") must not be negative");
        if (size <= index) throw new IndexOutOfBoundsException
            ("index (" + index + ") must be in range [0; " + Integer.toString(size - 1) + "]");
        return index;
    }

    private Preconditions()
    {
        throw new UnsupportedOperationException();
    }

}
