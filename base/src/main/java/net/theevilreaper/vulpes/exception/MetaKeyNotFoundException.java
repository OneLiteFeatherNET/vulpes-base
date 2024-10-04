package net.theevilreaper.vulpes.exception;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class MetaKeyNotFoundException extends RuntimeException {

    public MetaKeyNotFoundException(String message) {
        super(message);
    }

    public MetaKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaKeyNotFoundException(Throwable cause) {
        super(cause);
    }
}
