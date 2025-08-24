package net.theevilreaper.vulpes.registries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistryResourcesTest {

    @Test
    void testGetPath() {
        assertNotNull(RegistryResources.BITMAP_FONTS.getPath());
    }
}
