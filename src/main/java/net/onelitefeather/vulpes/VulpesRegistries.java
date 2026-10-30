package net.onelitefeather.vulpes;

import net.onelitefeather.vulpes.advancement.CustomAdvancement;
import net.onelitefeather.vulpes.attribute.CustomAttribute;
import net.onelitefeather.vulpes.font.CustomFontSymbol;
import net.onelitefeather.vulpes.item.CustomItem;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.sound.CustomSound;
import org.jetbrains.annotations.ApiStatus;

/**
 * The immutable registries of all content which was passed to {@link Vulpes#bootstrap(VulpesContent...)}.
 * <p>
 * The game keeps this instance and passes it to the services which need the entries.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface VulpesRegistries permits VulpesRegistriesImpl {

    /**
     * Returns the registry of all items.
     *
     * @return the item registry
     */
    Registry<CustomItem> items();

    /**
     * Returns the registry of all attributes.
     *
     * @return the attribute registry
     */
    Registry<CustomAttribute> attributes();

    /**
     * Returns the registry of all sounds.
     *
     * @return the sound registry
     */
    Registry<CustomSound> sounds();

    /**
     * Returns the registry of all font symbols.
     *
     * @return the font symbol registry
     */
    Registry<CustomFontSymbol> fontSymbols();

    /**
     * Returns the registry of all advancements.
     *
     * @return the advancement registry
     */
    Registry<CustomAdvancement> advancements();
}
