package net.onelitefeather.vulpes.expression;

import java.util.List;

/**
 * A function the game implements in Java, which formulas can call by name.
 *
 * <p>Arguments arrive as {@link java.math.BigDecimal} for numbers, {@link Boolean} for booleans and {@link String} for
 * strings. The result must be one of these types as well; any {@link Number} is read as a number.
 *
 * @since 0.6.0
 */
@FunctionalInterface
public interface NativeFunction {

    /**
     * Calls the function.
     *
     * @param arguments the arguments in call order
     * @return the result, a number, boolean or string
     */
    Object call(List<Object> arguments);
}
