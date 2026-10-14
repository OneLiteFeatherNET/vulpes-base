package net.onelitefeather.vulpes.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomItemImplTest {

    @BeforeAll
    static void bindRegistries() {
        MinecraftServer.init();
    }

    private static final Key KEY = Key.key("test", "item");

    @Test
    void acceptsValidValues() {
        ItemStack stack = ItemStack.of(Material.DIAMOND_SWORD);

        CustomItemImpl item = new CustomItemImpl(KEY, stack);

        assertEquals(KEY, item.key());
        assertEquals(stack, item.itemStack());
    }

    @Test
    void rejectsNullKey() {
        assertThrows(NullPointerException.class, () -> new CustomItemImpl(null, ItemStack.of(Material.STONE)),
                "A null key must be rejected");
    }

    @Test
    void rejectsNullItemStackNamingTheKey() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CustomItemImpl(KEY, null));

        assertTrue(ex.getMessage().contains("test:item"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsAirItemStack() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CustomItemImpl(KEY, ItemStack.AIR));

        assertTrue(ex.getMessage().contains("test:item"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void getThrowsForMissingItem() {
        assertThrows(IllegalStateException.class, () -> CustomItemImpl.get(Key.key("test", "missing")),
                "Unknown items must be reported");
    }

    @Test
    void valuesAreEmptyWithPlaceholderEntries() {
        assertTrue(CustomItem.values().isEmpty(), "Placeholder entries should yield no items");
        assertNull(CustomItem.fromKey(KEY), "Unknown key should resolve to null");
    }
}
