package net.onelitefeather.vulpes.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemComponentDecoderTest {

    @BeforeAll
    static void bindRegistries() {
        MinecraftServer.init();
    }

    @Test
    void appliesDecodedComponent() {
        ItemStack stack = ItemComponentDecoder.apply(ItemStack.builder(Material.STONE),
                "minecraft:custom_name", "{\"text\":\"Sword\"}").build();

        assertEquals(Component.text("Sword"), stack.get(DataComponents.CUSTOM_NAME),
                "The custom name should be decoded from JSON");
    }

    @Test
    void appliesRegistryBackedEnchantments() {
        ItemStack stack = ItemComponentDecoder.apply(ItemStack.builder(Material.DIAMOND_SWORD),
                "minecraft:enchantments", "{\"minecraft:sharpness\":5}").build();

        EnchantmentList enchantments = stack.get(DataComponents.ENCHANTMENTS);
        assertNotNull(enchantments, "The enchantments component should be set");
        assertEquals(5, enchantments.level(Enchantment.SHARPNESS),
                "Sharpness should be applied at level 5");
    }

    @Test
    void returnsTheGivenBuilder() {
        ItemStack.Builder builder = ItemStack.builder(Material.STONE);

        assertSame(builder, ItemComponentDecoder.apply(builder), "The same builder should be returned");
    }

    @Test
    void rejectsOddNumberOfArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> ItemComponentDecoder.apply(ItemStack.builder(Material.STONE), "minecraft:custom_name"),
                "A key without a value must be rejected");
    }

    @Test
    void rejectsUnknownComponentNamingTheKey() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> ItemComponentDecoder.apply(ItemStack.builder(Material.STONE), "minecraft:not_a_component", "1"));

        assertTrue(ex.getMessage().contains("minecraft:not_a_component"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsMalformedJsonNamingTheKey() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> ItemComponentDecoder.apply(ItemStack.builder(Material.STONE), "minecraft:custom_name", "{"));

        assertTrue(ex.getMessage().contains("minecraft:custom_name"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsValueThatCannotBeDecodedNamingTheKey() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> ItemComponentDecoder.apply(ItemStack.builder(Material.STONE), "minecraft:max_stack_size", "\"many\""));

        assertTrue(ex.getMessage().contains("minecraft:max_stack_size"), "Message should name the key: " + ex.getMessage());
    }
}
