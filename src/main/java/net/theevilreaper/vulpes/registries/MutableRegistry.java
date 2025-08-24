package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public sealed interface MutableRegistry<T extends VulpesKey> extends Registry<T> permits VulpesMutableRegistry {

    /**
     * Registers a new object in the registry.
     *
     * @param name   the name for the object
     * @param object the object to register
     * @return the registered object
     */
    default @NotNull VulpesKey register(@NotNull String name, @NotNull T object) {
        return register(Key.key(name), object);
    }

    /**
     * Registers a new object in the registry.
     *
     * @param key    the key for the object
     * @param object the object to register
     * @return the registered object
     */
    @NotNull VulpesKey register(@NotNull Key key, @NotNull T object);

    /**
     * Removes the given key from the registry.
     *
     * @param key the key to remove
     * @return true if the key was removed otherwise false
     * @throws UnsupportedOperationException if the registry does not support removal
     */
    boolean remove(@NotNull Key key) throws UnsupportedOperationException;

}
