package net.theevilreaper.vulpes.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The enum "ItemCategory" represents different categories or types of items.
 * Each category is associated with a unique identifier represented as a byte value.
 * This enum is commonly used to classify items based on their specific attributes or effects,
 * allowing for efficient organization and management of items within a game or application.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public enum ItemCategory {

    HEAL((byte) 0),
    DAMAGE((byte) 1),
    DAMAGE_RESISTANCE((byte) 2),
    MOVEMENT_SPEED((byte) 3),
    LUCK((byte) 4);

    private static final ItemCategory[] VALUES = values();
    private final byte id;

    /**
     * Creates a new entry for the enum.
     * @param id the id for the entry
     */
    ItemCategory(byte id) {
        this.id = id;
    }

    /**
     * Returns the id value from an entry.
     * @return the given value
     */
    public byte getId() {
        return id;
    }

    /**
     * Translates the id to a corresponding category values
     * @param id the id for the category entry
     * @return the entry which matches with the id otherwise null
     */
    public static @Nullable ItemCategory translate(byte id) {
        return VALUES[id];
    }

    /**
     * Returns an array which contains all entries from the enum.
     * @return the given array
     */
    public static @NotNull ItemCategory[] getValues() {
        return VALUES;
    }
}
