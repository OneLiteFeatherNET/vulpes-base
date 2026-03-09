package net.onelitefeather.vulpes.item;

import net.minestom.server.entity.Player;

/**
 * @author theEvilReaper
 * @since 0.1.0
 * @version 1.0.0
 */
@FunctionalInterface
public interface ItemFunction {

    /**
     * Handles what happen when the player receives the item.
     *
     * @param player The player who receives the item
     */
    void apply(Player player, ItemData itemData);
}
