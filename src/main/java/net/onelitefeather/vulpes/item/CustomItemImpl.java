package net.onelitefeather.vulpes.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

/**
 * Default implementation of {@link CustomItem}.
 *
 * @param key       the key of the item
 * @param itemStack the item stack of the item
 */
@ApiStatus.Internal
record CustomItemImpl(Key key, ItemStack itemStack) implements CustomItem {

    CustomItemImpl {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(itemStack, "itemStack of item " + key.asString());
        if (itemStack.isAir()) {
            throw new IllegalArgumentException("The item stack of item " + key.asString() + " must not be air");
        }
    }
}
