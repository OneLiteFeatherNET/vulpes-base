package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RegistryFactory {

    static  <T extends VulpesKey<T>> @NotNull Registry<T> createRegistry(@NotNull Key registryKey, Registry.@NotNull EntryLoader<T> loader) {
        List<T> loadedEntries = loader.get(registryKey.value());
        Map<Key, T> namespaces = HashMap.newHashMap(loadedEntries.size());

        for (T loadedEntry : loadedEntries) {
            namespaces.put(loadedEntry.key(), loadedEntry);
        }

        return new ImmutableRegistry<>(registryKey, namespaces);
    }
}
