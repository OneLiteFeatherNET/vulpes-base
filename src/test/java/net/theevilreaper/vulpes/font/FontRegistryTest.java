package net.theevilreaper.vulpes.font;

import net.theevilreaper.vulpes.registries.Registry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FontRegistryTest {

    @Test
    void testRegistryTest() {
        Registry<BitFontSymbol> registry = BitFontSymbol.REGISTRY;
        assertNotNull(registry);
        assertEquals(1, registry.size());
    }
}
