package net.onelitefeather.vulpes.attribute;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Default implementation of {@link CustomAttribute}.
 *
 * @param key            the key of the attribute
 * @param displayName    the display name of the attribute
 * @param translationKey the translation key of the attribute or null if none is set
 * @param minimumValue   the minimum value of the attribute
 * @param defaultValue   the default value of the attribute
 * @param maximumValue   the maximum value of the attribute
 * @param synced         whether the value of the attribute is synced to the client
 */
@ApiStatus.Internal
record CustomAttributeImpl(
        Key key,
        String displayName,
        @Nullable String translationKey,
        double minimumValue,
        double defaultValue,
        double maximumValue,
        boolean synced
) implements CustomAttribute {

    CustomAttributeImpl {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(displayName, "displayName of attribute " + key.asString());
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("The display name of attribute " + key.asString() + " must not be blank");
        }
        if (!Double.isFinite(minimumValue) || !Double.isFinite(defaultValue) || !Double.isFinite(maximumValue)) {
            throw new IllegalArgumentException("The values of attribute " + key.asString() + " must be finite");
        }
        if (minimumValue > defaultValue) {
            throw new IllegalArgumentException("The minimum value " + minimumValue + " of attribute "
                    + key.asString() + " exceeds the default value " + defaultValue);
        }
        if (defaultValue > maximumValue) {
            throw new IllegalArgumentException("The default value " + defaultValue + " of attribute "
                    + key.asString() + " exceeds the maximum value " + maximumValue);
        }
    }
}
