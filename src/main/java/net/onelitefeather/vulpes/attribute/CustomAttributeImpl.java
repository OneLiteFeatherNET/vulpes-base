package net.onelitefeather.vulpes.attribute;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.RegistryFactory;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

/**
 * Default implementation of {@link CustomAttribute}.
 *
 * @param key          the key of the attribute
 * @param displayName  the display name of the attribute
 * @param defaultValue the default value of the attribute
 * @param maximumValue the maximum value of the attribute
 */
@ApiStatus.Internal
record CustomAttributeImpl(Key key, String displayName, double defaultValue, double maximumValue)
        implements CustomAttribute {

    static final Registry<CustomAttribute> REGISTRY =
            RegistryFactory.of(Key.key("vulpes", "attribute"), CustomAttributeEntries.entries());

    CustomAttributeImpl {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(displayName, "displayName of attribute " + key.asString());
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("The display name of attribute " + key.asString() + " must not be blank");
        }
        if (!Double.isFinite(defaultValue) || !Double.isFinite(maximumValue)) {
            throw new IllegalArgumentException("The values of attribute " + key.asString() + " must be finite");
        }
        if (defaultValue > maximumValue) {
            throw new IllegalArgumentException("The default value " + defaultValue + " of attribute "
                    + key.asString() + " exceeds the maximum value " + maximumValue);
        }
    }

    /**
     * Returns the attribute for the given key.
     *
     * @param key the key to look up
     * @return the attribute
     * @throws IllegalStateException if the attribute is not registered
     */
    static CustomAttribute get(Key key) {
        CustomAttribute attribute = REGISTRY.get(key);
        if (attribute == null) {
            throw new IllegalStateException("Missing custom attribute " + key.asString());
        }
        return attribute;
    }
}
