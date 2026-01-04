package net.onelitefeather.vulpes.render.exception;

/**
 * Exception thrown when a number is out of the allowed range.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public class RangeException extends RuntimeException {

    /**
     * Constructs a new RangeException with the specified detail message.
     *
     * @param message the detail message
     */
    public RangeException(String message) {
        super(message);
    }
}
