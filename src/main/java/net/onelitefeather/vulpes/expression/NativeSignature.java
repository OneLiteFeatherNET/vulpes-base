package net.onelitefeather.vulpes.expression;

import java.util.Objects;

/**
 * A {@link NativeFunction} together with the number of arguments it takes.
 *
 * @param arity    the number of arguments, zero or more
 * @param function the implementation
 * @since 0.6.0
 */
public record NativeSignature(int arity, NativeFunction function) {

    /**
     * Validates the signature.
     *
     * @throws NullPointerException     if the function is {@code null}
     * @throws IllegalArgumentException if the arity is negative
     */
    public NativeSignature {
        if (arity < 0) {
            throw new IllegalArgumentException("Native function arity must not be negative, but was " + arity);
        }
        Objects.requireNonNull(function, "function");
    }
}
