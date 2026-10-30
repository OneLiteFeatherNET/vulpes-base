package net.onelitefeather.vulpes.attribute;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomAttributeImplTest {

    private static final Key KEY = Key.key("test", "attr");

    @Test
    void ofReturnsAttributeWithAllValues() {
        CustomAttribute attribute = CustomAttribute.of(KEY, "Speed", "attribute.speed", 0.0, 1.0, 10.0, true);

        assertEquals("Speed", attribute.displayName(), "The display name should be kept");
        assertEquals("attribute.speed", attribute.translationKey(), "The translation key should be kept");
        assertEquals(0.0, attribute.minimumValue(), "The minimum value should be kept");
        assertEquals(1.0, attribute.defaultValue(), "The default value should be kept");
        assertEquals(10.0, attribute.maximumValue(), "The maximum value should be kept");
        assertTrue(attribute.synced(), "The sync flag should be kept");
    }

    @Test
    void ofAcceptsMissingTranslationKey() {
        CustomAttribute attribute = CustomAttribute.of(KEY, "Speed", null, 0.0, 1.0, 10.0, false);

        assertNull(attribute.translationKey(), "A missing translation key should stay null");
    }

    @Test
    void ofAcceptsEqualMinimumDefaultAndMaximum() {
        assertDoesNotThrow(() -> CustomAttribute.of(KEY, "Speed", null, 5.0, 5.0, 5.0, false),
                "Bounds equal to each other are valid");
    }

    @Test
    void ofRejectsDefaultValueAboveMaximumNamingKey() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> CustomAttribute.of(KEY, "Speed", null, 0.0, 11.0, 10.0, false));

        assertTrue(ex.getMessage().contains("test:attr"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void ofRejectsMinimumValueAboveDefaultNamingKey() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> CustomAttribute.of(KEY, "Speed", null, 2.0, 1.0, 10.0, false));

        assertTrue(ex.getMessage().contains("test:attr"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void ofRejectsBlankDisplayName() {
        assertThrows(IllegalArgumentException.class, () -> CustomAttribute.of(KEY, "  ", null, 0.0, 1.0, 10.0, false),
                "Blank display names must be rejected");
    }

    @Test
    void ofRejectsNaNMinimumValue() {
        assertThrows(IllegalArgumentException.class,
                () -> CustomAttribute.of(KEY, "Speed", null, Double.NaN, 1.0, 10.0, false), "NaN must be rejected");
    }

    @Test
    void ofRejectsNaNDefaultValue() {
        assertThrows(IllegalArgumentException.class,
                () -> CustomAttribute.of(KEY, "Speed", null, 0.0, Double.NaN, 10.0, false), "NaN must be rejected");
    }

    @Test
    void ofRejectsInfiniteMaximumValue() {
        assertThrows(IllegalArgumentException.class,
                () -> CustomAttribute.of(KEY, "Speed", null, 0.0, 1.0, Double.POSITIVE_INFINITY, false),
                "Infinity must be rejected");
    }
}
