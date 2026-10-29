package net.onelitefeather.vulpes.attribute;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A custom attribute which is defined in code and registered through {@link net.onelitefeather.vulpes.VulpesContent}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomAttribute extends VulpesKey permits CustomAttributeImpl {

    /**
     * Returns the display name of the attribute.
     *
     * @return the display name
     */
    String displayName();

    /**
     * Creates a custom attribute which can be passed to {@link net.onelitefeather.vulpes.VulpesContent}.
     *
     * @param key            the key of the attribute
     * @param displayName    the display name of the attribute, must not be blank
     * @param translationKey the translation key of the attribute or null if none is set
     * @param minimumValue   the minimum value of the attribute
     * @param defaultValue   the default value of the attribute
     * @param maximumValue   the maximum value of the attribute
     * @param synced         whether the value of the attribute is synced to the client
     * @return the created attribute
     * @throws NullPointerException     if the key or the display name is null
     * @throws IllegalArgumentException if a value is not finite, the display name is blank or the values are not
     *                                  ordered as minimum &lt;= default &lt;= maximum
     */
    static CustomAttribute of(Key key, String displayName, @Nullable String translationKey, double minimumValue,
                              double defaultValue, double maximumValue, boolean synced) {
        return new CustomAttributeImpl(key, displayName, translationKey, minimumValue, defaultValue, maximumValue, synced);
    }

    /**
     * Returns the translation key of the attribute.
     *
     * @return the translation key or null if none is set
     */
    @Nullable String translationKey();

    /**
     * Returns the minimum value of the attribute.
     *
     * @return the minimum value
     */
    double minimumValue();

    /**
     * Returns whether the value of the attribute is synced to the client.
     *
     * @return true if the value is synced
     */
    boolean synced();

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
}
