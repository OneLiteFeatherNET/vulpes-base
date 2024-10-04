package net.theevilreaper.vulpes.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemCategoryTest {

    @Test
    void testItemCategory() {
        assertEquals((byte)0, ItemCategory.HEAL.getId());
    }

    @Test
    void testItemCategoryTranslate() {
        assertEquals(ItemCategory.DAMAGE, ItemCategory.translate((byte)1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ItemCategory.translate((byte)12));
    }

    @Test
    void testGetValues() {
        assertEquals(ItemCategory.values().length, ItemCategory.getValues().length);
    }
}
