package net.onelitefeather.vulpes.registries;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * The {@link Registry} interface contains the basic method structure for different kind of registries.
 *
 * @param <T> the type of the registry
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public sealed interface Registry<T extends VulpesKey> extends Keyed permits ImmutableRegistry, MutableRegistry {

    /**
     * Returns the size of the registry.
     *
     * @return the current size of the registry
     */
    int size();

    /**
     * Returns the value for the given key.
     *
     * @param key the key to get the value for
     * @return the value for the given key or null if the key is not registered
     */
    @Nullable T get(Key key);

    /**
     * Checks if the given key is registered.
     *
     * @param key the key to check
     * @return true if the key is registered otherwise false
     */
    default boolean contains(VulpesKey key) {
        return contains(key.key());
    }

    /**
     * Checks if the given key is registered.
     *
     * @param key the key to check
     * @return true if the key is registered otherwise false
     */
    boolean contains(Key key);

    /**
     * Returns all registered keys.
     * <p>
     * The order of the keys is not guaranteed.
     *
     * @return the collection of keys
     */
    Collection<Key> keys();

    /**
     * Returns all registered values.
     * The order of the keys is not guaranteed.
     *
     * @return the collection of values
     */
    Collection<T> values();

    @FunctionalInterface
    interface EntryLoader<T extends VulpesKey> {

        List<T> get(InputStream inputStream);
    }
}
