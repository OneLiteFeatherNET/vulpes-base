package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.AdvancementManager;
import net.minestom.server.advancements.AdvancementRoot;
import net.minestom.server.advancements.AdvancementTab;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Creates Minestom advancement tabs from {@link CustomAdvancement custom advancements}.
 * <p>
 * Every root becomes one tab which is identified by the string form of its key. All descendants are attached
 * in topological order, so a parent always exists before its children.
 *
 * @since 1.0.0
 */
public final class AdvancementTabs {

    private AdvancementTabs() {
    }

    /**
     * Returns the given root and all its descendants in topological order.
     *
     * @param root the root advancement
     * @return the root followed by its descendants, a parent always comes before its children
     * @throws IllegalArgumentException if the advancement is not a root
     */
    public static List<CustomAdvancement> tree(CustomAdvancement root) {
        return tree(CustomAdvancement.values(), root);
    }

    /**
     * Creates the tab of the given root with all its descendants.
     *
     * @param manager the manager which owns the tab
     * @param root    the root advancement
     * @return the created tab
     * @throws IllegalArgumentException if the advancement is not a root
     */
    public static AdvancementTab createTab(AdvancementManager manager, CustomAdvancement root) {
        return createTab(manager, CustomAdvancement.values(), root);
    }

    /**
     * Creates one tab for every registered root.
     *
     * @param manager the manager which owns the tabs
     * @return the created tabs by the key of their root, sorted by key
     */
    public static Map<Key, AdvancementTab> createAll(AdvancementManager manager) {
        return createAll(manager, CustomAdvancement.values());
    }

    static List<CustomAdvancement> tree(Collection<? extends CustomAdvancement> advancements, CustomAdvancement root) {
        Objects.requireNonNull(root, "root");
        if (!root.isRoot()) {
            throw new IllegalArgumentException("The advancement " + root.key().asString() + " is not a root");
        }
        return CustomAdvancementGraph.subtree(advancements, root);
    }

    static AdvancementTab createTab(AdvancementManager manager, Collection<? extends CustomAdvancement> advancements,
                                    CustomAdvancement root) {
        Objects.requireNonNull(manager, "manager");
        List<CustomAdvancement> order = tree(advancements, root);

        Map<Key, Advancement> created = new HashMap<>();
        Advancement rootAdvancement = root.create();
        created.put(root.key(), rootAdvancement);
        AdvancementTab tab = manager.createTab(root.key().asString(), (AdvancementRoot) rootAdvancement);

        for (CustomAdvancement advancement : order.subList(1, order.size())) {
            Advancement child = advancement.create();
            tab.createAdvancement(advancement.key().asString(), child, created.get(advancement.parent()));
            created.put(advancement.key(), child);
        }
        return tab;
    }

    static Map<Key, AdvancementTab> createAll(AdvancementManager manager, Collection<? extends CustomAdvancement> advancements) {
        Map<Key, AdvancementTab> tabs = new LinkedHashMap<>();
        for (CustomAdvancement root : CustomAdvancementGraph.roots(advancements)) {
            tabs.put(root.key(), createTab(manager, advancements, root));
        }
        return tabs;
    }
}
