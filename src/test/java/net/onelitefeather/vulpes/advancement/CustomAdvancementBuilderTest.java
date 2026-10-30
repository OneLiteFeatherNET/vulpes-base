package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomAdvancementBuilderTest {

    private static final Key KEY = Key.key("test", "adv");
    private static final Key PARENT = Key.key("test", "root");

    @Test
    void buildCreatesRootWithoutParent() {
        CustomAdvancement advancement = CustomAdvancement.builder(KEY)
                .title(Component.text("Title"))
                .icon(ItemStack.of(Material.STONE))
                .build();

        assertEquals(KEY, advancement.key(), "The advancement should keep its key");
        assertTrue(advancement.isRoot(), "An advancement without parent should be a root");
    }

    @Test
    void buildSetsParent() {
        CustomAdvancement advancement = CustomAdvancement.builder(KEY)
                .parent(PARENT)
                .title(Component.text("Title"))
                .icon(ItemStack.of(Material.STONE))
                .build();

        assertEquals(PARENT, advancement.parent(), "The parent should be kept");
    }

    @Test
    void buildRequiresTitleNamingKey() {
        CustomAdvancementBuilder builder = CustomAdvancement.builder(KEY).icon(ItemStack.of(Material.STONE));

        NullPointerException ex = assertThrows(NullPointerException.class, builder::build);

        assertTrue(ex.getMessage().contains("test:adv"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void buildKeepsBackgroundOfRoot() {
        CustomAdvancement advancement = CustomAdvancement.builder(KEY)
                .background("test:textures/bg.png")
                .title(Component.text("Title"))
                .icon(ItemStack.of(Material.STONE))
                .build();

        assertEquals("test:textures/bg.png", advancement.background(), "The background of a root should be kept");
    }
}
