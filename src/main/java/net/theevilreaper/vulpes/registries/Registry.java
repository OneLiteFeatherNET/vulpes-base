package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The {@link Registry} interface contains the basic method structure for different kind of registries.
 *
 * @param <T> the type of the registry
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public sealed interface Registry<T extends VulpesKey<T>> extends Keyed permits ImmutableRegistry, MutableRegistry {

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
    @Nullable T get(@NotNull Key key);

    /**
     * Returns the value for the given key.
     *
     * @param key the key to get the value for
     * @return the value for the given key or null if the key is not registered
     */
    default @Nullable T get(@NotNull VulpesKey<T> key) {
        return get(key.key());
    }

    /**
     * Checks if the given key is registered.
     *
     * @param key the key to check
     * @return true if the key is registered otherwise false
     */
    boolean contains(@NotNull VulpesKey<T> key);

    /**
     * Checks if the given key is registered.
     *
     * @param key the key to check
     * @return true if the key is registered otherwise false
     */
    boolean contains(@NotNull Key key);

    /**
     * Returns all registered keys.
     * <p>
     * The order of the keys is not guaranteed.
     *
     * @return the collection of keys
     */
    @NotNull Collection<Key> keys();

    /**
     * Returns all registered values.
     * The order of the keys is not guaranteed.
     *
     * @return the collection of values
     */
    @NotNull Collection<T> values();
}
