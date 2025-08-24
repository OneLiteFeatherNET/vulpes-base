package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import net.theevilreaper.vulpes.registries.helper.StringKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImmutableRegistryTest {

    private static Key registryKey;
    private static Registry<StringKey> staticRegistry;

    @BeforeAll
    static void setUp() {
        registryKey = Key.key("vulpes", "test");
        staticRegistry = RegistryFactory.createRegistry(registryKey, namespace -> {
            List<StringKey> entries = new ArrayList<>();
            entries.add(new StringKey(Key.key("vulpes", "test1"), "test"));
            entries.add(new StringKey(Key.key("vulpes", "test2"), "test2"));
            return entries;
        });

        assertInstanceOf(ImmutableRegistry.class, staticRegistry, "The static registry should be an immutable registry");
    }

    @Test
    void testBasicChecks() {
        assertEquals(2, staticRegistry.size());
        assertEquals(registryKey, staticRegistry.key());
        assertEquals(staticRegistry.keys().size(), staticRegistry.values().size());
    }

    @Test
    void testContains() {
        assertTrue(staticRegistry.contains(new StringKey(Key.key("vulpes", "test1"), "test")));
        assertFalse(staticRegistry.contains(Key.key("vulpes", "test3")));
    }

    @Test
    void testGet() {
        var fetchedKey = staticRegistry.get(Key.key("vulpes", "test1"));
        assertNotNull(fetchedKey);
        assertInstanceOf(StringKey.class, fetchedKey);
        assertEquals("test", fetchedKey.value());
    }
}