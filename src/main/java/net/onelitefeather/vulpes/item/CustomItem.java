package net.onelitefeather.vulpes.item;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A custom item which is defined in code and available through a runtime registry.
 * <p>
 * The constants are inherited from the generated {@code CustomItems} interface, the keys
 * are available through {@link CustomItemKeys}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomItem extends VulpesKey, CustomItems permits CustomItemImpl {

    /**
     * Returns the item stack of the item. Item stacks are immutable, so the instance can be shared.
     *
     * @return the item stack
     */
    ItemStack itemStack();

    /**
     * Returns the item for the given key.
     *
     * @param key the key to look up
     * @return the item or null if no item is registered for the key
     */
    static @Nullable CustomItem fromKey(Key key) {
        return CustomItemImpl.REGISTRY.get(key);
    }

    /**
     * Returns all registered items.
     *
     * @return the registered items
     */
    static Collection<CustomItem> values() {
        return CustomItemImpl.REGISTRY.values();
    }

    /**
     * Returns the registry which contains all custom items.
     *
     * @return the registry
     */
    static Registry<CustomItem> registry() {
        return CustomItemImpl.REGISTRY;
    }
}
