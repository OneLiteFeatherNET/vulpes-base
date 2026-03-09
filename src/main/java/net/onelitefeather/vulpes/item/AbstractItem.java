package net.onelitefeather.vulpes.item;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 **/
public class AbstractItem {

    private final UUID id = UUID.randomUUID();

    private transient ItemMetaData itemMetaData;

    private ItemFunction itemFunction;

    private ItemData itemData;

    private final ItemStack itemStack;

    public AbstractItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public AbstractItem(ItemStack itemStack, ItemData itemData) {
        this.itemStack = itemStack;
        this.itemData = itemData;
    }

    /**
     * Set a {@link ItemFunction} to the item class.
     * @param itemFunction the function to set
     */
    public void setItemFunction(ItemFunction itemFunction) {
        this.itemFunction = itemFunction;
    }

    /**
     * Applies the data from the item to a player.
     * @param player the player who should get the data
     */
    public void applyToPlayer(Player player) {
        if (itemFunction == null || itemData == null) return;
        this.itemFunction.apply(player, itemData);
    }

    public ItemMetaData getItemMetaData() {
        if (itemMetaData == null && getClass().isAnnotationPresent(ItemMetaData.class)) {
            this.itemMetaData = getClass().getAnnotation(ItemMetaData.class);
        }
        return itemMetaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractItem that = (AbstractItem) o;
        return Objects.equals(id, that.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns the {@link ItemStack} behind the item.
     * @return the given {@link ItemStack}
     */
    public ItemStack getItem() {
        return itemStack;
    }

    /**
     * Returns the {@link ItemType} from the item.
     * @return the given {@link ItemType}
     */
    public ItemType getItemCategory() {
        return itemMetaData.type();
    }

    /**
     * Returns the {@link ItemRarity} from the item.
     * @return the given {@link ItemRarity}
     */
    public ItemRarity getItemRarity() {
        return itemMetaData.rarity();
    }

    /**
     * Returns the {@link ItemData} from the item.
     * @return the given {@link ItemData} if set otherwise null
     */
    @Nullable
    public ItemData getItemData() {
        return itemData;
    }

    /**
     * Returns the uuid from the item.
     * @return the given uuid
     */
    public UUID getId() {
        return id;
    }
}
