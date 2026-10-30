package net.onelitefeather.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

@ApiStatus.Internal
final class ImmutableRegistry<T extends VulpesKey> implements Registry<T> {

    private final Key key;
    private final Map<Key, T> dataMap;
    private final Map<Key, Key> aliases;

    /**
     * Creates a new static registry.
     * @param key the key for the registry
     * @param dataMap the data map for the registry
     * @param aliases the alias keys which resolve to a key of the data map
     */
    ImmutableRegistry(Key key, Map<Key, T> dataMap, Map<Key, Key> aliases) {
        this.key = key;
        this.dataMap = Map.copyOf(dataMap);
        this.aliases = Map.copyOf(aliases);
    }

    /**
     * Returns the entry of the given key or of the key the alias points to.
     *
     * @param key the key or alias to look up
     * @return the entry or null if neither the key nor its alias target is registered
     */
    private @Nullable T entry(Key key) {
        T entry = this.dataMap.get(key);
        if (entry == null && this.aliases.containsKey(key)) {
            entry = this.dataMap.get(this.aliases.get(key));
        }
        return entry;
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
    public @Nullable T get(Key key) {
        return entry(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(VulpesKey key) {
        return contains(key.key());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Key key) {
        return entry(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Key> keys() {
        return this.dataMap.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> values() {
        return this.dataMap.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Key key() {
        return this.key;
    }
}
