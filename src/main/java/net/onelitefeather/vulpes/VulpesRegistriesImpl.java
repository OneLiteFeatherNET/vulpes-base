package net.onelitefeather.vulpes;

import net.onelitefeather.vulpes.advancement.CustomAdvancement;
import net.onelitefeather.vulpes.attribute.CustomAttribute;
import net.onelitefeather.vulpes.font.CustomFontSymbol;
import net.onelitefeather.vulpes.item.CustomItem;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.sound.CustomSound;
import org.jetbrains.annotations.ApiStatus;

/**
 * Default implementation of {@link VulpesRegistries}.
 *
 * @param items        the item registry
 * @param attributes   the attribute registry
 * @param sounds       the sound registry
 * @param fontSymbols  the font symbol registry
 * @param advancements the advancement registry
 */
@ApiStatus.Internal
record VulpesRegistriesImpl(
        Registry<CustomItem> items,
        Registry<CustomAttribute> attributes,
        Registry<CustomSound> sounds,
        Registry<CustomFontSymbol> fontSymbols,
        Registry<CustomAdvancement> advancements
) implements VulpesRegistries {
}
