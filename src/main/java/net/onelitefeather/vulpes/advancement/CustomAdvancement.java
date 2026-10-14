package net.onelitefeather.vulpes.advancement;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.Advancement;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.ItemStack;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A custom advancement which is defined in code and available through a runtime registry.
 * <p>
 * The definition is immutable. Minestom advancements are mutable and bound to one tab, so
 * {@link #create()} returns a new instance on every call. The constants are inherited from the generated
 * {@code CustomAdvancements} interface, the keys are available through {@link CustomAdvancementKeys}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomAdvancement extends VulpesKey, CustomAdvancements permits CustomAdvancementImpl {

    /**
     * Returns the key of the parent advancement.
     *
     * @return the key of the parent or null if this advancement is a root
     */
    @Nullable Key parent();

    /**
     * Returns the title of the advancement.
     *
     * @return the title
     */
    Component title();

    /**
     * Returns the description of the advancement.
     *
     * @return the description
     */
    Component description();

    /**
     * Returns the icon of the advancement.
     *
     * @return the icon
     */
    ItemStack icon();

    /**
     * Returns the frame of the advancement.
     *
     * @return the frame type
     */
    FrameType frame();

    /**
     * Returns the horizontal position in the advancement tab.
     *
     * @return the x position
     */
    float x();

    /**
     * Returns the vertical position in the advancement tab.
     *
     * @return the y position
     */
    float y();

    /**
     * Returns whether a toast is shown when the advancement is achieved.
     *
     * @return true if a toast is shown
     */
    boolean showToast();

    /**
     * Returns whether the advancement stays hidden until it is achieved.
     *
     * @return true if the advancement is hidden
     */
    boolean hidden();

    /**
     * Returns the background resource location of the tab.
     *
     * @return the background or null if none is set, always null for advancements which are not a root
     */
    @Nullable String background();

    /**
     * Returns whether this advancement has no parent and therefore starts a tab.
     *
     * @return true if this advancement is a root
     */
    default boolean isRoot() {
        return parent() == null;
    }

    /**
     * Creates a new Minestom advancement from this definition. A root creates an
     * {@link net.minestom.server.advancements.AdvancementRoot}.
     *
     * @return a new advancement, never a shared instance
     */
    @Contract(value = "-> new", pure = true)
    Advancement create();

    /**
     * Returns the advancement for the given key.
     *
     * @param key the key to look up
     * @return the advancement or null if no advancement is registered for the key
     */
    static @Nullable CustomAdvancement fromKey(Key key) {
        return CustomAdvancementImpl.REGISTRY.get(key);
    }

    /**
     * Returns all registered advancements.
     *
     * @return the registered advancements
     */
    static Collection<CustomAdvancement> values() {
        return CustomAdvancementImpl.REGISTRY.values();
    }

    /**
     * Returns the registry which contains all custom advancements.
     *
     * @return the registry
     */
    static Registry<CustomAdvancement> registry() {
        return CustomAdvancementImpl.REGISTRY;
    }

    /**
     * Returns all roots sorted by their key.
     *
     * @return the roots
     */
    static List<CustomAdvancement> roots() {
        return CustomAdvancementGraph.roots(CustomAdvancementImpl.REGISTRY.values());
    }

    /**
     * Returns the direct children of the given advancement sorted by their key.
     *
     * @param parent the key of the parent
     * @return the children, empty if there are none
     */
    static List<CustomAdvancement> children(Key parent) {
        return CustomAdvancementGraph.children(CustomAdvancementImpl.REGISTRY.values(), parent);
    }
}
