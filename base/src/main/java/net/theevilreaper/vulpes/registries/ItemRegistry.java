package net.theevilreaper.vulpes.registries;

import net.theevilreaper.vulpes.exception.MetaKeyNotFoundException;
import net.theevilreaper.vulpes.item.AbstractItem;
import net.theevilreaper.vulpes.item.ItemMetaData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phillippglanz
 * @since 1.0.0
 * @version 1.0.0
 */
@ApiStatus.Experimental
public final class ItemRegistry {

    private final Map<String, AbstractItem> itemMap;

    public ItemRegistry() {
        this.itemMap = new HashMap<>();
    }

    /**
     * Add a new {@link AbstractItem} instance to the map.
     * Note the item must include the {@link ItemMetaData}, if the class does not have the annotation.
     * Then the item will not be added and a {@link RuntimeException} will be thrown.
     * @param abstractItem the instance to the item which should be added
     */
    public void addItem(@NotNull AbstractItem abstractItem) {
        if (abstractItem.getClass().isAnnotationPresent(ItemMetaData.class)) {
            ItemMetaData annotation = abstractItem.getClass().getAnnotation(ItemMetaData.class);
            this.itemMap.putIfAbsent(annotation.key(), abstractItem);
        } else {
            throw new MetaKeyNotFoundException("No ItemMetaData annotation found");
        }
    }

    /**
     * Get a instance of an {@link AbstractItem} by the given key.
     * @param key the key from the item
     * @return the instance from the {@link AbstractItem} if the key is in the map
     * @param <T> the class from the item must extends from {@link AbstractItem}
     */
    @Nullable
    public <T extends AbstractItem> T getItem(@NotNull String key) {
        return (T)  this.itemMap.getOrDefault(key, null);
    }

    /**
     * Check if a given key has a corresponding {@link AbstractItem} in the map.
      * @param key the key to check
     * @return True if there is a {@link AbstractItem} which matches with the key otherwise false
     */
    public boolean hasItem(@NotNull String key) {
        return this.itemMap.containsKey(key);
    }

    /**
     * Checks if the given {@link AbstractItem} is in the given map.
     * @param abstractItem the item to check
     * @return True if the item is in the map otherwise false
     */
    public boolean hasItem(@NotNull AbstractItem abstractItem) {
        return this.itemMap.containsValue(abstractItem);
    }

    /**
     * Remove a {@link AbstractItem} by his corresponding key.
     * @param key the key from the {@link AbstractItem}
     */
    public void removeItem(@NotNull String key) {
        this.itemMap.remove(key);
    }
}
