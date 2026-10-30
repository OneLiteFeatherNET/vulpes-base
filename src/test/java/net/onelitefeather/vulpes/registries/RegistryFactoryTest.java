package net.onelitefeather.vulpes.registries;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegistryFactoryTest {


    private static final Key REGISTRY_KEY = Key.key("test", "registry");

    @Test
    void ofResolvesEntriesByKey() {
        TestEntry entry = new TestEntry(Key.key("test", "a"));
        Registry<TestEntry> registry = RegistryFactory.of(REGISTRY_KEY, List.of(entry));

        assertSame(entry, registry.get(entry.key()), "TestEntry should be found by its key");
        assertEquals(1, registry.size(), "Registry should contain one entry");
        assertEquals(REGISTRY_KEY, registry.key(), "Registry should keep its key");
    }

    @Test
    void ofRejectsDuplicateKeys() {
        Key key = Key.key("test", "dup");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> RegistryFactory.of(REGISTRY_KEY, List.of(new TestEntry(key), new TestEntry(key))));

        assertTrue(ex.getMessage().contains("test:dup"), "Message should name the key: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("test:registry"), "Message should name the registry: " + ex.getMessage());
    }

    @Test
    void ofRejectsNullEntries() {
        assertThrows(NullPointerException.class,
                () -> RegistryFactory.of(REGISTRY_KEY, Arrays.asList(new TestEntry(Key.key("test", "a")), null)),
                "Null entries must be rejected");
    }

    @Test
    void ofResolvesAliasToCurrentEntry() {
        TestEntry entry = new TestEntry(Key.key("test", "new"));
        Key alias = Key.key("test", "old");
        Registry<TestEntry> registry = RegistryFactory.of(REGISTRY_KEY, List.of(entry), Map.of(alias, entry.key()));

        assertSame(entry, registry.get(alias), "The alias should resolve to the current entry");
    }

    @Test
    void ofDoesNotListAliasAsKey() {
        TestEntry entry = new TestEntry(Key.key("test", "new"));
        Registry<TestEntry> registry = RegistryFactory.of(REGISTRY_KEY, List.of(entry),
                Map.of(Key.key("test", "old"), entry.key()));

        assertEquals(List.of(entry.key()), List.copyOf(registry.keys()), "Alias keys should not be listed");
        assertEquals(1, registry.size(), "Aliases should not count as entries");
    }

    @Test
    void ofIgnoresAliasWhoseTargetIsNotInRegistry() {
        TestEntry entry = new TestEntry(Key.key("test", "a"));
        Key alias = Key.key("test", "old");
        Registry<TestEntry> registry = RegistryFactory.of(REGISTRY_KEY, List.of(entry),
                Map.of(alias, Key.key("test", "missing")));

        assertNull(registry.get(alias), "An alias without a target in this registry should resolve to null");
    }
}
