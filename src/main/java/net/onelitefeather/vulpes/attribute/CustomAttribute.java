package net.onelitefeather.vulpes.attribute;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A custom attribute which is defined in code and available through a runtime registry.
 * <p>
 * The constants are inherited from the generated {@code CustomAttributes} interface, the keys
 * are available through {@link CustomAttributeKeys}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomAttribute extends VulpesKey, CustomAttributes permits CustomAttributeImpl {

    /**
     * Returns the display name of the attribute.
     *
     * @return the display name
     */
    String displayName();

    /**
     * Returns the default value of the attribute.
     *
     * @return the default value
     */
    double defaultValue();

    /**
     * Returns the maximum value of the attribute.
     *
     * @return the maximum value
     */
    double maximumValue();

    /**
     * Returns the attribute for the given key.
     *
     * @param key the key to look up
     * @return the attribute or null if no attribute is registered for the key
     */
    static @Nullable CustomAttribute fromKey(Key key) {
        return CustomAttributeImpl.REGISTRY.get(key);
    }

    /**
     * Returns all registered attributes.
     *
     * @return the registered attributes
     */
    static Collection<CustomAttribute> values() {
        return CustomAttributeImpl.REGISTRY.values();
    }

    /**
     * Returns the registry which contains all custom attributes.
     *
     * @return the registry
     */
    static Registry<CustomAttribute> registry() {
        return CustomAttributeImpl.REGISTRY;
    }
}
