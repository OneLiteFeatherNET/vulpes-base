package net.theevilreaper.vulpes.api.meta.functional;

import org.jetbrains.annotations.NotNull;

/**
 * This functional interface is used to add metadata to a given object.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
@FunctionalInterface
public interface MetaDataAdder {

    /**
     * Add a key with a value to the metadata.
     * @param key the key which corresponds to the value
     * @param value the value which corresponds to the key
     * @return the added value
     * @param <T> the type of the value
     */
    <T> T addData(@NotNull String key, @NotNull T value);
}
