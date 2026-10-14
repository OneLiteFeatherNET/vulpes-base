package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.AdvancementManager;
import net.minestom.server.advancements.AdvancementTab;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdvancementTabsTest {

    private static CustomAdvancement adv(String path, String parent) {
        return new CustomAdvancementImpl(Key.key("test", path), parent == null ? null : Key.key("test", parent),
                Component.text(path), Component.empty(), ItemStack.of(Material.STONE), FrameType.TASK,
                0f, 0f, false, false, null);
    }

    @Test
    void treeRejectsNonRoot() {
        List<CustomAdvancement> all = List.of(adv("root", null), adv("child", "root"));

        assertThrows(IllegalArgumentException.class, () -> AdvancementTabs.tree(all, all.get(1)),
                "Only a root can start a tab");
    }

    @Test
    void createTabUsesRootKeyAsIdentifier() {
        List<CustomAdvancement> all = List.of(adv("root", null), adv("child", "root"));
        AdvancementManager manager = new AdvancementManager();

        AdvancementTab tab = AdvancementTabs.createTab(manager, all, all.get(0));

        assertSame(tab, manager.getTab("test:root"), "The tab must be registered under the key of its root");
    }

    @Test
    void createAllCreatesOneTabPerRoot() {
        List<CustomAdvancement> all = List.of(adv("one", null), adv("two", null), adv("child", "one"));
        AdvancementManager manager = new AdvancementManager();

        Map<Key, AdvancementTab> tabs = AdvancementTabs.createAll(manager, all);

        assertEquals(List.of(Key.key("test", "one"), Key.key("test", "two")), List.copyOf(tabs.keySet()));
    }

    @Test
    void createTabWithoutRegisteredAdvancementsCreatesNoTabs() {
        assertTrue(AdvancementTabs.createAll(new AdvancementManager()).isEmpty(), "Placeholder entries have no roots");
    }
}
