package net.theevilreaper.vulpes.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemTypeTest {

    @Test
    void testItemType() {
        assertNotEquals(ItemType.ACTIVE, ItemType.PASSIVE);
    }
}
