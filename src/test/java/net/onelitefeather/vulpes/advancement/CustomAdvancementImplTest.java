package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.AdvancementRoot;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomAdvancementImplTest {

    private static final Key KEY = Key.key("test", "adv");
    private static final Key PARENT = Key.key("test", "root");

    private static CustomAdvancementImpl advancement(Key key, Key parent, float x, float y, String background) {
        return new CustomAdvancementImpl(key, parent, Component.text("Title"), Component.text("Description"),
                ItemStack.of(Material.STONE), FrameType.GOAL, x, y, true, true, background);
    }

    @Test
    void acceptsValidValues() {
        CustomAdvancementImpl advancement = advancement(KEY, PARENT, 1.5f, -2f, null);

        assertEquals(PARENT, advancement.parent());
        assertEquals(Component.text("Title"), advancement.title());
        assertFalse(advancement.isRoot(), "An advancement with a parent is not a root");
    }

    @Test
    void rejectsNullTitle() {
        assertThrows(NullPointerException.class, () -> new CustomAdvancementImpl(KEY, null, null,
                Component.empty(), ItemStack.of(Material.STONE), FrameType.TASK, 0, 0, false, false, null),
                "A missing title must be rejected");
    }

    @Test
    void rejectsNaNPosition() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> advancement(KEY, null, Float.NaN, 0f, null), "NaN must be rejected");

        assertTrue(ex.getMessage().contains("test:adv"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsInfinitePosition() {
        assertThrows(IllegalArgumentException.class, () -> advancement(KEY, null, 0f, Float.POSITIVE_INFINITY, null),
                "Infinity must be rejected");
    }

    @Test
    void rejectsSelfParent() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> advancement(KEY, KEY, 0f, 0f, null), "A self parent must be rejected");

        assertTrue(ex.getMessage().contains("test:adv"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsBackgroundOnChild() {
        assertThrows(IllegalArgumentException.class, () -> advancement(KEY, PARENT, 0f, 0f, "bg.png"),
                "Only roots can have a background");
    }

    @Test
    void createReturnsRootWithBackgroundForRoot() {
        Advancement created = advancement(KEY, null, 0f, 0f, "bg.png").create();

        assertInstanceOf(AdvancementRoot.class, created, "A root must create an AdvancementRoot");
    }

    @Test
    void createReturnsPlainAdvancementForChild() {
        Advancement created = advancement(KEY, PARENT, 0f, 0f, null).create();

        assertFalse(created instanceof AdvancementRoot, "A child must not create an AdvancementRoot");
    }

    @Test
    void createReturnsNewInstanceOnEveryCall() {
        CustomAdvancementImpl advancement = advancement(KEY, PARENT, 0f, 0f, null);

        assertNotSame(advancement.create(), advancement.create(), "Minestom advancements are mutable and must not be shared");
    }

    @Test
    void createCopiesFlagsToAdvancement() {
        Advancement created = advancement(KEY, PARENT, 0f, 0f, null).create();

        assertTrue(created.isHidden(), "The hidden flag must be copied");
    }

    @Test
    void getThrowsForMissingAdvancement() {
        assertThrows(IllegalStateException.class, () -> CustomAdvancementImpl.get(Key.key("test", "missing")),
                "Unknown advancements must be reported");
    }

    @Test
    void valuesAreEmptyWithPlaceholderEntries() {
        assertTrue(CustomAdvancement.values().isEmpty(), "Placeholder entries should yield no advancements");
        assertNull(CustomAdvancement.fromKey(KEY), "Unknown key should resolve to null");
        assertTrue(CustomAdvancement.roots().isEmpty(), "Without entries there are no roots");
        assertTrue(CustomAdvancement.children(PARENT).isEmpty(), "Without entries there are no children");
    }
}
