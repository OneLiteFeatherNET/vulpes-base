package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class VulpesMutableRegistry<T extends VulpesKey> implements MutableRegistry<T> {

    private static final Object LOCK = new Object();
    private final Key key;
    private final Map<Key, T> dataMap;

    /**
     * Creates a new static registry.
     * @param key the key for the registry
     * @param dataMap the data map for the registry
     */
    VulpesMutableRegistry(@NotNull Key key, @NotNull Map<Key, T> dataMap) {
        this.key = key;
        this.dataMap = new HashMap<>(dataMap);
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
    public boolean contains(@NotNull VulpesKey key) {
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

    @Override
    public @NotNull VulpesKey register(@NotNull Key key, @NotNull T object) {
        synchronized (LOCK) {
            this.dataMap.put(key, object);
        }
        return object;
    }

    @Override
    public boolean remove(@NotNull Key key) throws UnsupportedOperationException {
        return false;
    }
}
