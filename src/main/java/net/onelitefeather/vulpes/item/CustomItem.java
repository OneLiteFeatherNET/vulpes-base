package net.onelitefeather.vulpes.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;

/**
 * A custom item which is defined in code and registered through {@link net.onelitefeather.vulpes.VulpesContent}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomItem extends VulpesKey permits CustomItemImpl {

    /**
     * Returns the item stack of the item. Item stacks are immutable, so the instance can be shared.
     *
     * @return the item stack
     */
    ItemStack itemStack();

    /**
     * Creates a custom item which can be passed to {@link net.onelitefeather.vulpes.VulpesContent}.
     *
     * @param key       the key of the item
     * @param itemStack the item stack of the item, must not be air
     * @return the created item
     * @throws NullPointerException     if the key or the item stack is null
     * @throws IllegalArgumentException if the item stack is air
     */
    static CustomItem of(Key key, ItemStack itemStack) {
        return new CustomItemImpl(key, itemStack);
    }
}
