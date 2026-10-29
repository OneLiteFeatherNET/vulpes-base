package net.onelitefeather.vulpes;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.advancement.CustomAdvancement;
import net.onelitefeather.vulpes.attribute.CustomAttribute;
import net.onelitefeather.vulpes.font.CustomFontSymbol;
import net.onelitefeather.vulpes.item.CustomItem;
import net.onelitefeather.vulpes.sound.CustomSound;

import java.util.List;
import java.util.Map;

/**
 * Content for tests. Every test creates its own instance, so no test depends on another one.
 *
 * @param items        the items of the content
 * @param attributes   the attributes of the content
 * @param sounds       the sounds of the content
 * @param fontSymbols  the font symbols of the content
 * @param advancements the advancements of the content
 * @param aliases      the alias keys of the content
 */
public record TestContent(
        List<CustomItem> items,
        List<CustomAttribute> attributes,
        List<CustomSound> sounds,
        List<CustomFontSymbol> fontSymbols,
        List<CustomAdvancement> advancements,
        Map<Key, Key> aliases
) implements VulpesContent {

    /**
     * Creates content which contains only the given items.
     *
     * @param items the items
     * @return the content
     */
    public static TestContent withItems(CustomItem... items) {
        return new TestContent(List.of(items), List.of(), List.of(), List.of(), List.of(), Map.of());
    }

    /**
     * Creates content which contains only the given advancements.
     *
     * @param advancements the advancements
     * @return the content
     */
    public static TestContent withAdvancements(CustomAdvancement... advancements) {
        return new TestContent(List.of(), List.of(), List.of(), List.of(), List.of(advancements), Map.of());
    }

    /**
     * Returns a copy of this content with the given alias keys.
     *
     * @param aliases the alias keys
     * @return the content with the aliases
     */
    public TestContent withAliases(Map<Key, Key> aliases) {
        return new TestContent(items, attributes, sounds, fontSymbols, advancements, aliases);
    }
}
