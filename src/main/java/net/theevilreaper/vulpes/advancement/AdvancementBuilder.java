package net.theevilreaper.vulpes.advancement;

import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Builder implementation to build {@link Advancement}.
 * @author theEvilReape
 * @version 1.0.0
 * @since 1.0.0
 */
public interface AdvancementBuilder  {

    /**
     * Creates a new instance of the {@link AdvancementBuilderImpl}.
     * @return the created instance
     */
    @Contract(pure = true)
    static @NotNull Builder builder() {
        return new AdvancementBuilderImpl();
    }

    /**
     * Creates a new instance of the {@link AdvancementBuilderImpl}.
     * @param title the title for the {@link Advancement} as {@link Component}
     * @return the created instance
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull Builder of(@NotNull Component title) {
        return new AdvancementBuilderImpl(title);
    }

    /**
     * Contains all method to build an {@link Advancement}.
     */
    sealed interface Builder permits AdvancementBuilderImpl {

        /**
         * Set the achieved state in the {@link Advancement}.
         * @param achieved the new state to set
         * @return AdvancementBuilder
         */
        @NotNull Builder achieved(boolean achieved);

        /**
         * Set if the {@link Advancement} should use a toast.
         * @param toast the new state to set
         * @return AdvancementBuilder
         */
        @NotNull Builder showToast(boolean toast);

        /**
         * Set the hidden state in the {@link Advancement}.
         * @param hidden the new state to set
         * @return AdvancementBuilder
         */
        @NotNull Builder hidden(boolean hidden);

        /**
         * Set the title to the {@link Advancement}.
         * @param title the title as {@link Component}
         * @return AdvancementBuilder
         */
        @Contract(value = "_ -> this")
        @NotNull Builder title(@NotNull Component title);

        /**
         * Set the description to the {@link Advancement}.
         * @param description the description to set
         * @return AdvancementBuilder
         */
        @Contract(value = "_ -> this")
        @NotNull Builder description(@NotNull Component description);

        /**
         * Set the icon for the {@link Advancement}.
         * @param icon the icon to set as {@link ItemStack}
         * @return AdvancementBuilder
         */
        @Contract(value = "_ -> this")
        @NotNull Builder icon(@NotNull ItemStack icon);

        /**
         * Set the {@link FrameType} to the {@link Advancement}.
         * @param frameType the type to set
         * @return AdvancementBuilder
         */
        @Contract(value = "_ -> this")
        @NotNull Builder type(@NotNull FrameType frameType);

        /**
         * Set's the x value for the {@link Advancement}.
         * @param x the x value to set
         * @return AdvancementBuilder
         */
        @Contract(value = "_ -> this")
        @NotNull Builder x(float x);

        /**
         * Set's the y value for the {@link Advancement}.
         * @param y the y value to set
         * @return AdvancementBuilder
         */
        @Contract(value = "_ -> this")
        @NotNull Builder y(float y);

        /**
         * Returns a new instance of the {@link Advancement} with the given values.
         * @return the created instance
         */
        @Contract(value = "-> new", pure = true)
        @NotNull Advancement build();
    }
}
