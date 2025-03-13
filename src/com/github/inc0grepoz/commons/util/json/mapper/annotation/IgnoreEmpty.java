package com.github.inc0grepoz.commons.util.json.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Labels fields for the serializer to ignore, if they
 * store empty {@code Collection} or {@code Map} instances.
 * 
 * @author inc0g-repoz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreEmpty
{}
