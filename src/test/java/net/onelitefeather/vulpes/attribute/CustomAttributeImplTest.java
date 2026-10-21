package net.onelitefeather.vulpes.attribute;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomAttributeImplTest {

    private static final Key KEY = Key.key("test", "attr");

    @Test
    void acceptsValidValues() {
        CustomAttributeImpl attribute = new CustomAttributeImpl(KEY, "Speed", 1.0, 10.0);

        assertEquals("Speed", attribute.displayName());
        assertEquals(1.0, attribute.defaultValue());
        assertEquals(10.0, attribute.maximumValue());
    }

    @Test
    void rejectsDefaultValueAboveMaximum() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CustomAttributeImpl(KEY, "Speed", 11.0, 10.0));

        assertTrue(ex.getMessage().contains("test:attr"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsBlankDisplayName() {
        assertThrows(IllegalArgumentException.class, () -> new CustomAttributeImpl(KEY, "  ", 1.0, 10.0),
                "Blank display names must be rejected");
    }

    @Test
    void rejectsNaNDefaultValue() {
        assertThrows(IllegalArgumentException.class, () -> new CustomAttributeImpl(KEY, "Speed", Double.NaN, 10.0),
                "NaN must be rejected");
    }

    @Test
    void rejectsInfiniteMaximumValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomAttributeImpl(KEY, "Speed", 1.0, Double.POSITIVE_INFINITY),
                "Infinity must be rejected");
    }

    @Test
    void getThrowsForMissingAttribute() {
        assertThrows(IllegalStateException.class, () -> CustomAttributeImpl.get(Key.key("test", "missing")),
                "Unknown attributes must be reported");
    }

    @Test
    void valuesAreEmptyWithPlaceholderEntries() {
        assertTrue(CustomAttribute.values().isEmpty(), "Placeholder entries should yield no attributes");
        assertNull(CustomAttribute.fromKey(KEY), "Unknown key should resolve to null");
    }
}
