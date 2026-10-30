package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.AdvancementRoot;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Default implementation of {@link CustomAdvancement}.
 *
 * @param key         the key of the advancement
 * @param parent      the key of the parent, null for a root
 * @param title       the title of the advancement
 * @param description the description of the advancement
 * @param icon        the icon of the advancement
 * @param frame       the frame of the advancement
 * @param x           the horizontal position
 * @param y           the vertical position
 * @param showToast   whether a toast is shown when the advancement is achieved
 * @param hidden      whether the advancement stays hidden until it is achieved
 * @param background  the background of the tab, only allowed for a root
 */
@ApiStatus.Internal
record CustomAdvancementImpl(
        Key key,
        @Nullable Key parent,
        Component title,
        Component description,
        ItemStack icon,
        FrameType frame,
        float x,
        float y,
        boolean showToast,
        boolean hidden,
        @Nullable String background
) implements CustomAdvancement {

    CustomAdvancementImpl {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(title, "title of advancement " + key.asString());
        Objects.requireNonNull(description, "description of advancement " + key.asString());
        Objects.requireNonNull(icon, "icon of advancement " + key.asString());
        Objects.requireNonNull(frame, "frame of advancement " + key.asString());
        if (!Float.isFinite(x) || !Float.isFinite(y)) {
            throw new IllegalArgumentException("The position of advancement " + key.asString()
                    + " must be finite but was x=" + x + ", y=" + y);
        }
        if (key.equals(parent)) {
            throw new IllegalArgumentException("The advancement " + key.asString() + " must not be its own parent");
        }
        if (parent != null && background != null) {
            throw new IllegalArgumentException("Only a root can have a background but advancement "
                    + key.asString() + " has the parent " + parent.asString());
        }
    }

    @Override
    public Advancement create() {
        if (parent == null) {
            AdvancementRoot root = new AdvancementRoot(title, description, icon, frame, x, y, background);
            root.showToast(showToast);
            root.setHidden(hidden);
            return root;
        }
        return AdvancementBuilder.builder()
                .title(title)
                .description(description)
                .icon(icon)
                .type(frame)
                .x(x)
                .y(y)
                .showToast(showToast)
                .hidden(hidden)
                .build();
    }
}
