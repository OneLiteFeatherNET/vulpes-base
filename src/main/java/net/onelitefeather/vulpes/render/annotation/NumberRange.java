package net.onelitefeather.vulpes.render.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define a number range and starting Unicode for digit mapping.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NumberRange {

    /**
     * Minimum number in the range.
     *
     * @return the minimum number
     */
    int min() default 0;

    /**
     * Maximum number in the range.
     *
     * @return the maximum number
     */
    int max();

    /**
     * Starting Unicode code point for digit '0'.
     *
     * @return the starting Unicode code point
     */
    int startUnicode();
}
