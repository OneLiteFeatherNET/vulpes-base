package net.onelitefeather.vulpes;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.advancement.CustomAdvancement;
import net.onelitefeather.vulpes.advancement.CustomAdvancementGraph;
import net.onelitefeather.vulpes.attribute.CustomAttribute;
import net.onelitefeather.vulpes.font.CustomFontSymbol;
import net.onelitefeather.vulpes.item.CustomItem;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.RegistryFactory;
import net.onelitefeather.vulpes.registries.VulpesKey;
import net.onelitefeather.vulpes.sound.CustomSound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Entry point of the library which turns {@link VulpesContent contents} into immutable {@link VulpesRegistries}.
 * <p>
 * The library keeps no global state: every call of {@link #bootstrap(VulpesContent...)} returns its own registries.
 *
 * @since 1.0.0
 */
public final class Vulpes {

    private static final Key ITEM_REGISTRY = Key.key("vulpes", "item");
    private static final Key ATTRIBUTE_REGISTRY = Key.key("vulpes", "attribute");
    private static final Key SOUND_REGISTRY = Key.key("vulpes", "sound");
    private static final Key FONT_SYMBOL_REGISTRY = Key.key("vulpes", "font_symbol");
    private static final Key ADVANCEMENT_REGISTRY = Key.key("vulpes", "advancement");

    private Vulpes() {
    }

    /**
     * Merges the given contents, validates them and returns the registries of all entries.
     * <p>
     * The validation covers the following rules:
     * <ul>
     *     <li>a key occurs at most once per type across all contents,</li>
     *     <li>an alias key is declared at most once and is not the key of an entry,</li>
     *     <li>an alias points to an entry of some type,</li>
     *     <li>every advancement parent exists and the parents contain no cycle.</li>
     * </ul>
     *
     * @param contents the contents to register, may be empty
     * @return the immutable registries of all contents
     * @throws NullPointerException     if the array, one of the contents or one of their elements is null
     * @throws IllegalArgumentException if a key occurs twice within a type or an alias is invalid, the message names the key
     * @throws IllegalStateException    if an advancement parent is missing or the parents form a cycle, the message names the keys
     */
    public static VulpesRegistries bootstrap(VulpesContent... contents) {
        Objects.requireNonNull(contents, "contents");
        List<CustomItem> items = new ArrayList<>();
        List<CustomAttribute> attributes = new ArrayList<>();
        List<CustomSound> sounds = new ArrayList<>();
        List<CustomFontSymbol> fontSymbols = new ArrayList<>();
        List<CustomAdvancement> advancements = new ArrayList<>();
        Map<Key, Key> aliases = new HashMap<>();

        for (VulpesContent content : contents) {
            Objects.requireNonNull(content, "content");
            collect(items, content.items(), "item");
            collect(attributes, content.attributes(), "attribute");
            collect(sounds, content.sounds(), "sound");
            collect(fontSymbols, content.fontSymbols(), "font symbol");
            collect(advancements, content.advancements(), "advancement");
            collectAliases(aliases, content.aliases());
        }

        Set<Key> entryKeys = new HashSet<>();
        keysOf(entryKeys, items);
        keysOf(entryKeys, attributes);
        keysOf(entryKeys, sounds);
        keysOf(entryKeys, fontSymbols);
        keysOf(entryKeys, advancements);
        validateAliases(aliases, entryKeys);

        Registry<CustomAdvancement> advancementRegistry = RegistryFactory.of(ADVANCEMENT_REGISTRY, advancements, aliases);
        CustomAdvancementGraph.validate(advancementRegistry.values());

        return new VulpesRegistriesImpl(
                RegistryFactory.of(ITEM_REGISTRY, items, aliases),
                RegistryFactory.of(ATTRIBUTE_REGISTRY, attributes, aliases),
                RegistryFactory.of(SOUND_REGISTRY, sounds, aliases),
                RegistryFactory.of(FONT_SYMBOL_REGISTRY, fontSymbols, aliases),
                advancementRegistry
        );
    }

    private static <T> void collect(List<T> target, Collection<? extends T> source, String type) {
        Objects.requireNonNull(source, () -> "The " + type + "s of a content must not be null");
        for (T entry : source) {
            target.add(Objects.requireNonNull(entry, () -> "A content contains a null " + type));
        }
    }

    private static void collectAliases(Map<Key, Key> aliases, Map<Key, Key> contentAliases) {
        Objects.requireNonNull(contentAliases, "The aliases of a content must not be null");
        for (Map.Entry<Key, Key> alias : contentAliases.entrySet()) {
            Key key = Objects.requireNonNull(alias.getKey(), "A content contains a null alias key");
            Key target = Objects.requireNonNull(alias.getValue(), "The alias " + key.asString() + " has no target");
            if (aliases.putIfAbsent(key, target) != null) {
                throw new IllegalArgumentException("The alias " + key.asString() + " is declared more than once");
            }
        }
    }

    private static void keysOf(Set<Key> target, Collection<? extends VulpesKey> entries) {
        for (VulpesKey entry : entries) {
            target.add(entry.key());
        }
    }

    private static void validateAliases(Map<Key, Key> aliases, Set<Key> entryKeys) {
        for (Map.Entry<Key, Key> alias : aliases.entrySet()) {
            if (entryKeys.contains(alias.getKey())) {
                throw new IllegalArgumentException("The alias " + alias.getKey().asString()
                        + " collides with the entry of the same key");
            }
            if (!entryKeys.contains(alias.getValue())) {
                throw new IllegalArgumentException("The alias " + alias.getKey().asString()
                        + " points to " + alias.getValue().asString() + " which has no entry");
            }
        }
    }
}
