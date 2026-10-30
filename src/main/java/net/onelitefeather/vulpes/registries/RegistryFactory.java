package net.onelitefeather.vulpes.registries;

import net.kyori.adventure.key.Key;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface RegistryFactory {

    /**
     * Creates a new instance of a {@link ImmutableRegistry<>} with the given parameters.
     *
     * @param registryKey       the key for the registry
     * @param loader            the loader for the registry entries
     * @param registryResources the registry resources to load the entries from
     * @param <T>               the type of the registry entries
     * @return the created registry instance
     */
    static <T extends VulpesKey> Registry<T> createRegistry(
            Key registryKey,
            Registry.EntryLoader<T> loader,
            RegistryResources registryResources
    ) {
        List<T> loadedEntries = loader.get(RegistryData.getRegistryData(registryResources));
        Map<Key, T> namespaces = HashMap.newHashMap(loadedEntries.size());

        for (T loadedEntry : loadedEntries) {
            namespaces.put(loadedEntry.key(), loadedEntry);
        }

        return new ImmutableRegistry<>(registryKey, namespaces, Map.of());
    }

    /**
     * Creates an immutable registry from entries which are defined in code.
     *
     * @param registryKey the key for the registry
     * @param entries     the entries of the registry
     * @param <T>         the type of the registry entries
     * @return the created registry instance
     * @throws NullPointerException     if the entries or one of them is null
     * @throws IllegalArgumentException if two entries share the same key
     */
    static <T extends VulpesKey> Registry<T> of(Key registryKey, Collection<? extends T> entries) {
        return of(registryKey, entries, Map.of());
    }

    /**
     * Creates an immutable registry from entries which are defined in code and resolves alias keys.
     * <p>
     * An alias resolves to its target entry only if the target is an entry of this registry. Aliases are not
     * listed in {@link Registry#keys()} or {@link Registry#values()}.
     *
     * @param registryKey the key for the registry
     * @param entries     the entries of the registry
     * @param aliases     the alias keys mapped to the keys of their current entries
     * @param <T>         the type of the registry entries
     * @return the created registry instance
     * @throws NullPointerException     if the entries, the aliases or one of their elements is null
     * @throws IllegalArgumentException if two entries share the same key
     */
    static <T extends VulpesKey> Registry<T> of(Key registryKey, Collection<? extends T> entries, Map<Key, Key> aliases) {
        Objects.requireNonNull(registryKey, "registryKey");
        Objects.requireNonNull(entries, "entries");
        Objects.requireNonNull(aliases, "aliases");
        Map<Key, T> namespaces = HashMap.newHashMap(entries.size());

        for (T entry : entries) {
            Objects.requireNonNull(entry, "Registry " + registryKey.asString() + " contains a null entry");
            if (namespaces.putIfAbsent(entry.key(), entry) != null) {
                throw new IllegalArgumentException("Duplicate key " + entry.key().asString()
                        + " in registry " + registryKey.asString());
            }
        }

        Map<Key, Key> resolvable = new HashMap<>();
        for (Map.Entry<Key, Key> alias : aliases.entrySet()) {
            Objects.requireNonNull(alias.getKey(), "Registry " + registryKey.asString() + " contains a null alias");
            Objects.requireNonNull(alias.getValue(), "Alias " + alias.getKey().asString() + " has no target");
            if (!namespaces.containsKey(alias.getKey()) && namespaces.containsKey(alias.getValue())) {
                resolvable.put(alias.getKey(), alias.getValue());
            }
        }

        return new ImmutableRegistry<>(registryKey, namespaces, resolvable);
    }
}
