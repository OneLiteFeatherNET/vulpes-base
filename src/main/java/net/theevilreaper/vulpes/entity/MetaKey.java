package net.theevilreaper.vulpes.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author phillippglanz
 * @version 1.0.0
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaKey {
    /**
     * Returns the name for the key.
     * @return the given name
     */
    String value();
}
