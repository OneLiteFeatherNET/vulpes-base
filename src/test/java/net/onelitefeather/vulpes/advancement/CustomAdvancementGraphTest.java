package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomAdvancementGraphTest {

    private static Key key(String path) {
        return Key.key("test", path);
    }

    private static CustomAdvancement adv(String path, String parent) {
        return new CustomAdvancementImpl(key(path), parent == null ? null : key(parent), Component.text(path),
                Component.empty(), ItemStack.of(Material.STONE), FrameType.TASK, 0f, 0f, false, false, null);
    }

    private static List<String> paths(List<CustomAdvancement> advancements) {
        return advancements.stream().map(advancement -> advancement.key().value()).toList();
    }

    @Test
    void validateAcceptsValidTree() {
        assertDoesNotThrow(() -> CustomAdvancementGraph.validate(List.of(adv("root", null), adv("child", "root"))),
                "A valid tree must pass");
    }

    @Test
    void validateRejectsMissingParentNamingKey() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> CustomAdvancementGraph.validate(List.of(adv("orphan", "ghost"))), "Missing parents must be rejected");

        assertTrue(ex.getMessage().contains("test:orphan") && ex.getMessage().contains("test:ghost"),
                "Message should name the key and parent: " + ex.getMessage());
    }

    @Test
    void validateRejectsCycleNamingKey() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> CustomAdvancementGraph.validate(List.of(adv("a", "b"), adv("b", "a"))), "Cycles must be rejected");

        assertTrue(ex.getMessage().contains("test:a"), "Message should name a key of the cycle: " + ex.getMessage());
    }

    @Test
    void rootsAreSortedByKey() {
        List<CustomAdvancement> all = List.of(adv("zeta", null), adv("alpha", null), adv("child", "alpha"));

        assertEquals(List.of("alpha", "zeta"), paths(CustomAdvancementGraph.roots(all)));
    }

    @Test
    void childrenAreDirectOnlyAndSortedByKey() {
        List<CustomAdvancement> all = List.of(adv("root", null), adv("b", "root"), adv("a", "root"), adv("deep", "a"));

        assertEquals(List.of("a", "b"), paths(CustomAdvancementGraph.children(all, key("root"))));
    }

    @Test
    void subtreePutsParentsBeforeChildrenDepthFirst() {
        List<CustomAdvancement> all = List.of(adv("root", null), adv("beta", "root"), adv("alpha", "root"),
                adv("gamma", "alpha"), adv("other", null));

        List<CustomAdvancement> subtree = CustomAdvancementGraph.subtree(all, all.get(0));

        assertEquals(List.of("root", "alpha", "gamma", "beta"), paths(subtree));
    }
}
