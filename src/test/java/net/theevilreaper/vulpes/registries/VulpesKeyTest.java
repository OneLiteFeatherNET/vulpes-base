package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Key;
import net.theevilreaper.vulpes.registries.helper.StringKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VulpesKeyTest {

    static Key key;

    @BeforeAll
    static void setUp() {
        key = Key.key("vulpes", "test");
    }

    @Test
    void testKey() {
        VulpesKey vulpesKey = new StringKey(key, "test");
        assertNotNull(vulpesKey);
        assertEquals(key, vulpesKey.key());
    }
}
