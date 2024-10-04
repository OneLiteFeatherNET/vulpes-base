package net.theevilreaper.vulpes.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemRarityTest {

    @Test
    void testRarityEnum() {
        assertNotEquals(ItemRarity.RARE.getDisplay(), ItemRarity.COMMON.getDisplay());
    }

    @Test
    void testWeight() {
        assertEquals(20.0D, ItemRarity.COMMON.getWeight());
    }

    @Test
    void testGetValues() {
        assertEquals(ItemRarity.values().length, ItemRarity.getValues().length);
    }
}
