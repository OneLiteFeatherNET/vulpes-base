package net.onelitefeather.vulpes.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.RegistryFactory;
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

    static final Registry<CustomItem> REGISTRY =
            RegistryFactory.of(Key.key("vulpes", "item"), CustomItemEntries.entries());

    CustomItemImpl {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(itemStack, "itemStack of item " + key.asString());
        if (itemStack.isAir()) {
            throw new IllegalArgumentException("The item stack of item " + key.asString() + " must not be air");
        }
    }

    /**
     * Returns the item for the given key.
     *
     * @param key the key to look up
     * @return the item
     * @throws IllegalStateException if the item is not registered
     */
    static CustomItem get(Key key) {
        CustomItem item = REGISTRY.get(key);
        if (item == null) {
            throw new IllegalStateException("Missing custom item " + key.asString());
        }
        return item;
    }
}
