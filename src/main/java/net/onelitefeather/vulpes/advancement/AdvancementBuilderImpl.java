package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;

/**
 * Implementation of the {@link AdvancementBuilder.Builder}.
 * @author theEvilReaper
 * @version 1.1.0
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
     * {@inheritDoc}
     */
    AdvancementBuilderImpl(Component title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder achieved(boolean achieved) {
        this.achieved = achieved;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder showToast(boolean toast) {
        this.toast = toast;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder hidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder title(Component title) {
        this.title = title;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder description(Component description) {
        this.description = description;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder icon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder type(FrameType frameType) {
        this.frameType = frameType;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder x(float x) {
        this.x = x;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvancementBuilder.Builder y(float y) {
        this.y = y;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Advancement build() {
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
