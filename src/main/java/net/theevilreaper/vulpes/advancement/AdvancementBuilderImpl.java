package net.theevilreaper.vulpes.advancement;

import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of the {@link AdvancementBuilder.Builder}.
 * @author theEvilReaper
 * @version 1.0.0
 */
public non-sealed class AdvancementBuilderImpl implements AdvancementBuilder.Builder {

    private Component title;
    private Component description;
    private FrameType frameType;
    private boolean toast;
    private boolean hidden;
    private boolean achieved;
    private ItemStack icon;
    private float x;
    private float y;

    /**
     * Creates a new instance from the builder.
     */
    AdvancementBuilderImpl() {}

    /**
     * Creates a new instance from the builder with a given title {@link Component}.
     * @param title the given component
     */
    AdvancementBuilderImpl(@NotNull Component title) {
        this.title = title;
    }

    /**
     * Set's the state if the {@link Advancement} is achieved by someone.
     * @param achieved the new state to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder achieved(boolean achieved) {
        this.achieved = achieved;
        return this;
    }

    /**
     * Set if the {@link Advancement} shows a toast message.
     * @param toast the new state to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder showToast(boolean toast) {
        this.toast = toast;
        return this;
    }

    /**
     * Set the state if the {@link Advancement} is hidden or not.
     * @param hidden the new state to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder hidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * Set's the title as {@link Component} to the {@link Advancement}.
     * @param title the title as {@link Component}
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    /**
     * Set's the description as {@link Component} to the {@link Advancement}.
     * @param description the description to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder description(@NotNull Component description) {
        this.description = description;
        return this;
    }

    /**
     * Set the icon as {@link ItemStack} to the {@link Advancement}.
     * @param icon the icon to set as {@link ItemStack}
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder icon(@NotNull ItemStack icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Set's the {@link FrameType} for the {@link Advancement}.
     * @param frameType the type to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder type(@NotNull FrameType frameType) {
        this.frameType = frameType;
        return this;
    }

    /**
     * Set's the x position for the {@link Advancement}.
     * @param x the x value to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder x(float x) {
        this.x = x;
        return this;
    }

    /**
     * Set's the y position for the {@link Advancement}.
     * @param y the y value to set
     * @return the builder instance
     */
    @Override
    public AdvancementBuilder.@NotNull Builder y(float y) {
        this.y = y;
        return this;
    }

    /**
     * Returns a created instance from an {@link Advancement}.
     * @return the created instance
     */
    @Override
    public @NotNull Advancement build() {
        if (this.title == null) {
            throw new IllegalArgumentException("The title can not be null");
        }

        if (this.icon == null) {
            throw new IllegalArgumentException("The icon can not be null");
        }

        if (this.frameType == null) {
            throw new IllegalArgumentException("The frame type can not be null");
        }

        var advancement = new Advancement(this.title, this.description, this.icon, this.frameType, this.x, this.y);
        advancement.setAchieved(this.achieved);
        advancement.setHidden(this.hidden);
        advancement.showToast(this.toast);
        return advancement;
    }
}
