package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Builder for a {@link CustomAdvancement}, created with {@link CustomAdvancement#builder(Key)}.
 * <p>
 * The title and the icon are required. The description is empty, the frame is a task, and the
 * position, the toast and the hidden flag keep their defaults unless they are set.
 *
 * @since 1.0.0
 */
public final class CustomAdvancementBuilder {

    private final Key key;
    private @Nullable Key parent;
    private @Nullable Component title;
    private Component description = Component.empty();
    private @Nullable ItemStack icon;
    private FrameType frame = FrameType.TASK;
    private float x;
    private float y;
    private boolean showToast;
    private boolean hidden;
    private @Nullable String background;

    CustomAdvancementBuilder(Key key) {
        this.key = Objects.requireNonNull(key, "key");
    }

    /**
     * Sets the key of the parent advancement, null makes the advancement a root.
     *
     * @param parent the key of the parent or null
     * @return this builder
     */
    public CustomAdvancementBuilder parent(@Nullable Key parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Sets the title of the advancement.
     *
     * @param title the title
     * @return this builder
     */
    public CustomAdvancementBuilder title(Component title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the description of the advancement.
     *
     * @param description the description
     * @return this builder
     */
    public CustomAdvancementBuilder description(Component description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the icon of the advancement.
     *
     * @param icon the icon
     * @return this builder
     */
    public CustomAdvancementBuilder icon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets the frame of the advancement.
     *
     * @param frame the frame type
     * @return this builder
     */
    public CustomAdvancementBuilder frame(FrameType frame) {
        this.frame = frame;
        return this;
    }

    /**
     * Sets the horizontal position in the advancement tab.
     *
     * @param x the x position
     * @return this builder
     */
    public CustomAdvancementBuilder x(float x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the vertical position in the advancement tab.
     *
     * @param y the y position
     * @return this builder
     */
    public CustomAdvancementBuilder y(float y) {
        this.y = y;
        return this;
    }

    /**
     * Sets whether a toast is shown when the advancement is achieved.
     *
     * @param showToast true to show a toast
     * @return this builder
     */
    public CustomAdvancementBuilder showToast(boolean showToast) {
        this.showToast = showToast;
        return this;
    }

    /**
     * Sets whether the advancement stays hidden until it is achieved.
     *
     * @param hidden true to hide the advancement
     * @return this builder
     */
    public CustomAdvancementBuilder hidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * Sets the background of the tab, only allowed for a root.
     *
     * @param background the background resource location or null
     * @return this builder
     */
    public CustomAdvancementBuilder background(@Nullable String background) {
        this.background = background;
        return this;
    }

    /**
     * Creates the advancement from the values of this builder.
     *
     * @return the created advancement
     * @throws NullPointerException     if the title or the icon is not set
     * @throws IllegalArgumentException if the values are invalid, the message names the key
     */
    public CustomAdvancement build() {
        return new CustomAdvancementImpl(key, parent, title, description, icon, frame, x, y, showToast, hidden,
                background);
    }
}
