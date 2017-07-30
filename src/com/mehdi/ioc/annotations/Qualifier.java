package com.mehdi.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * in case of finding a component with two implementations, we use this annotations as a tie breaker
 *
 * @author Mehdi Maick
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Qualifier {
    String name() default "";
}
