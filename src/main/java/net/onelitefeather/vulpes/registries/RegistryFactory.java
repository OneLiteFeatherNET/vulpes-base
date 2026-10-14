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

        return new ImmutableRegistry<>(registryKey, namespaces);
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
        Objects.requireNonNull(registryKey, "registryKey");
        Objects.requireNonNull(entries, "entries");
        Map<Key, T> namespaces = HashMap.newHashMap(entries.size());

        for (T entry : entries) {
            Objects.requireNonNull(entry, "Registry " + registryKey.asString() + " contains a null entry");
            if (namespaces.putIfAbsent(entry.key(), entry) != null) {
                throw new IllegalArgumentException("Duplicate key " + entry.key().asString()
                        + " in registry " + registryKey.asString());
            }
        }

        return new ImmutableRegistry<>(registryKey, namespaces);
    }
}
