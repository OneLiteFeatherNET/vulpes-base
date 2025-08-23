package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

@ApiStatus.Internal
final class ImmutableRegistry<T extends VulpesKey<T>> implements Registry<T> {

    private final Key key;
    private final Map<Key, T> dataMap;

    /**
     * Creates a new static registry.
     * @param key the key for the registry
     * @param dataMap the data map for the registry
     */
    ImmutableRegistry(@NotNull Key key, @NotNull Map<Key, T> dataMap) {
        this.key = key;
        this.dataMap = Map.copyOf(dataMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.dataMap.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable T get(@NotNull Key key) {
        return this.dataMap.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull VulpesKey<T> key) {
        return this.dataMap.containsKey(key.key());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(@NotNull Key key) {
        return this.dataMap.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Collection<Key> keys() {
        return this.dataMap.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Collection<T> values() {
        return this.dataMap.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Key key() {
        return this.key;
    }
}
