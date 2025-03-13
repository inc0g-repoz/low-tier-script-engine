package com.github.inc0grepoz.commons.util.json.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the implementation class to the deserializer.
 * 
 * <p>This annotation must have at least one class specified,
 * if declared. Otherwise, an {@link IllegalStateException}
 * is thrown by the deserializer.
 * 
 * @author inc0g-repoz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Implementation
{

    /**
     * Returns the implementations array. If not empty,
     * the first {@code Class} is used to initialize an
     * instance.
     * 
     * @return the implementation classes array
     */
    Class<?>[] value() default {};

}
