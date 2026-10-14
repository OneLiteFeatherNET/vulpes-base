package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validates and orders the parent relations of custom advancements.
 * <p>
 * Siblings are always sorted by their key, so the result is deterministic.
 */
@ApiStatus.Internal
final class CustomAdvancementGraph {

    private static final Comparator<CustomAdvancement> BY_KEY = Comparator.comparing(advancement -> advancement.key().asString());

    private CustomAdvancementGraph() {
    }

    /**
     * Checks that every parent exists and that the parent relations contain no cycle.
     *
     * @param advancements the advancements to check
     * @throws IllegalStateException if a parent is missing or a cycle exists, the message names the key
     */
    static void validate(Collection<? extends CustomAdvancement> advancements) {
        Map<Key, CustomAdvancement> byKey = new HashMap<>();
        for (CustomAdvancement advancement : advancements) {
            byKey.put(advancement.key(), advancement);
        }

        for (CustomAdvancement advancement : sorted(advancements)) {
            Key parent = advancement.parent();
            if (parent != null && !byKey.containsKey(parent)) {
                throw new IllegalStateException("The advancement " + advancement.key().asString()
                        + " has the missing parent " + parent.asString());
            }
        }

        for (CustomAdvancement advancement : sorted(advancements)) {
            Set<Key> visited = new HashSet<>();
            Key current = advancement.key();
            while (current != null) {
                if (!visited.add(current)) {
                    throw new IllegalStateException("The advancement " + advancement.key().asString()
                            + " is part of a parent cycle");
                }
                current = byKey.get(current).parent();
            }
        }
    }

    /**
     * Returns the advancements without a parent sorted by key.
     */
    static List<CustomAdvancement> roots(Collection<? extends CustomAdvancement> advancements) {
        return advancements.stream().filter(CustomAdvancement::isRoot).sorted(BY_KEY).map(CustomAdvancement.class::cast).toList();
    }

    /**
     * Returns the direct children of the given parent sorted by key.
     */
    static List<CustomAdvancement> children(Collection<? extends CustomAdvancement> advancements, Key parent) {
        return advancements.stream().filter(advancement -> parent.equals(advancement.parent())).sorted(BY_KEY).map(CustomAdvancement.class::cast).toList();
    }

    /**
     * Returns the given root and all its descendants depth first, so a parent always comes before its children.
     *
     * @param advancements all advancements
     * @param root         the start of the subtree
     * @return the subtree in topological order
     */
    static List<CustomAdvancement> subtree(Collection<? extends CustomAdvancement> advancements, CustomAdvancement root) {
        Map<Key, List<CustomAdvancement>> childrenOf = new HashMap<>();
        for (CustomAdvancement advancement : sorted(advancements)) {
            if (advancement.parent() != null) {
                childrenOf.computeIfAbsent(advancement.parent(), ignored -> new ArrayList<>()).add(advancement);
            }
        }
        List<CustomAdvancement> order = new ArrayList<>();
        visit(root, childrenOf, order);
        return List.copyOf(order);
    }

    private static void visit(CustomAdvancement advancement, Map<Key, List<CustomAdvancement>> childrenOf, List<CustomAdvancement> order) {
        order.add(advancement);
        for (CustomAdvancement child : childrenOf.getOrDefault(advancement.key(), List.of())) {
            visit(child, childrenOf, order);
        }
    }

    private static List<CustomAdvancement> sorted(Collection<? extends CustomAdvancement> advancements) {
        return advancements.stream().sorted(BY_KEY).map(CustomAdvancement.class::cast).toList();
    }
}
