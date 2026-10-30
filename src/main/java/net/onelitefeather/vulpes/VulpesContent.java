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
 * The content of one content project, for example a generated set of items and sounds of a game.
 * <p>
 * A content project implements this interface and passes its instance to {@link Vulpes#bootstrap(VulpesContent...)}
 * at server start. The entries are built with the public factories of this library, so the content does not depend
 * on any generated type of {@code vulpes-base}.
 *
 * @since 0.5.0
 */
public interface VulpesContent {

    /**
     * Returns the items of this content.
     *
     * @return the items
     */
    List<CustomItem> items();

    /**
     * Returns the attributes of this content.
     *
     * @return the attributes
     */
    List<CustomAttribute> attributes();

    /**
     * Returns the sounds of this content.
     *
     * @return the sounds
     */
    List<CustomSound> sounds();

    /**
     * Returns the font symbols of this content.
     *
     * @return the font symbols
     */
    List<CustomFontSymbol> fontSymbols();

    /**
     * Returns the advancements of this content.
     *
     * @return the advancements
     */
    List<CustomAdvancement> advancements();

    /**
     * Returns the deprecated keys of renamed entries mapped to the keys of their current entries.
     * <p>
     * An alias key resolves to the current entry in the registries returned by {@link Vulpes#bootstrap(VulpesContent...)}.
     *
     * @return the alias keys, empty if no entry was renamed
     */
    Map<Key, Key> aliases();
}
