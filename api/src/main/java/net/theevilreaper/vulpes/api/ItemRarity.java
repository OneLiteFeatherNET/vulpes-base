package net.theevilreaper.vulpes.api;

import org.jetbrains.annotations.NotNull;

/**
 * The enum represents different rarity levels for items and includes several rarity options.
 * Each rarity level is associated with a specific weight and a descriptive label.
 * This enum is commonly used to categorize items based on their rarity, allowing for easy identification and manipulation in code.
 * The rarity levels are defined with decreasing probabilities, indicating that "NORMAL" items are the most common,
 * followed by "COMMON" and "RARE" items, which are progressively more difficult to obtain.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
public enum ItemRarity {

    NORMAL(70.0, "Normal"),
    COMMON(20.0, "Common"),
    RARE(10.0, "Rare");

    private static final ItemRarity[] VALUES = values();
    private final double weight;
    private final String display;

    /**
     * Creates a new enum entry with the given values.
     * @param weight the weight for the entry
     * @param display the display name from the entry
     */
    ItemRarity(double weight, @NotNull String display) {
        this.weight = weight;
        this.display = display;
    }

    /**
     * Return the given weight value from an entry.
     * @return the weight value
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the given display name as String from the entry.
     * @return the display value
     */
    public @NotNull String getDisplay() {
        return display;
    }

    /**
     * Returns an array which contains all entries from the enum.
     * @return the given array
     */
    public static @NotNull ItemRarity[] getValues() {
        return VALUES;
    }
}
