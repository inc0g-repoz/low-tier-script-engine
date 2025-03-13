package com.github.inc0grepoz.commons.util.json.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the type parameter classes implementations
 * to the deserializer.
 * 
 * @author inc0g-repoz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TypeParameters
{

    /**
     * Returns the type parameter classes implementations array.
     * 
     * @return the type parameter classes implementations array
     */
    Class<?>[] value() default {};

}
