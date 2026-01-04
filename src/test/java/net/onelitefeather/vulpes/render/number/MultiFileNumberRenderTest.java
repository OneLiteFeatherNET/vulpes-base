package net.onelitefeather.vulpes.render.number;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.onelitefeather.vulpes.render.NumberFontRenderer;
import net.onelitefeather.vulpes.render.exception.RangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MultiFileNumberRenderTest {

    private NumberFontRenderer renderer;

    // Unicode values from the JSON
    // First row: \uE120-\uE129 (0-9)
    // Second row: \uE12A-\uE133 (0-9 alternative)
    private static final int FIRST_ROW_START = 0xE120;
    private static final int SECOND_ROW_START = 0xE12A;

    @BeforeEach
    void setUp() {
        renderer = new NumberFontRenderer(
                Key.key("manis", "event_pass"),
                0,
                9999,
                new int[]{FIRST_ROW_START, SECOND_ROW_START}
        );
    }

    @Test
    @DisplayName("Single digit converts correctly to Unicode")
    void testSingleDigit() {
        Component result = renderer.renderNumber(5, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);

        // Digit 5 should become \uE125
        assertEquals("\uE125", text);
    }

    @Test
    @DisplayName("Two-digit number converts correctly")
    void testTwoDigits() {
        Component result = renderer.renderNumber(42, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);

        // 4 -> \uE124, 2 -> \uE122
        assertEquals("\uE124\uE122", text);
    }

    @Test
    @DisplayName("Number 0 converts correctly")
    void testZero() {
        Component result = renderer.renderNumber(0, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);

        // 0 -> \uE120
        assertEquals("\uE120", text);
    }

    @Test
    @DisplayName("Four-digit number converts correctly")
    void testFourDigits() {
        Component result = renderer.renderNumber(1337, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);

        // 1 -> \uE121, 3 -> \uE123, 3 -> \uE123, 7 -> \uE127
        assertEquals("\uE121\uE123\uE123\uE127", text);
    }

    @Test
    @DisplayName("All digits 0-9 are mapped correctly")
    void testAllDigits() {
        for (int i = 0; i <= 9; i++) {
            Component result = renderer.renderNumber(i, false);
            String text = PlainTextComponentSerializer.plainText().serialize(result);

            char expected = (char) (FIRST_ROW_START + i);
            assertEquals(String.valueOf(expected), text,
                    "Digit " + i + " should convert to \\u" + Integer.toHexString(expected).toUpperCase());
        }
    }

    @Test
    @DisplayName("Maximum allowed number (9999) works")
    void testMaxValue() {
        Component result = renderer.renderNumber(9999, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);

        // 9 -> \uE129 (four times)
        assertEquals("\uE129\uE129\uE129\uE129", text);
    }

    @Test
    @DisplayName("Font key is set correctly")
    void testFontKey() {
        Component result = renderer.renderNumber(123, false);

        assertEquals(Key.key("manis", "event_pass"), result.font());
    }

    @Test
    @DisplayName("Shadow mode works correctly")
    void testShadowMode() {
        Component withShadow = renderer.renderNumber(42, true);
        Component withoutShadow = renderer.renderNumber(42, false);

        // Both should have the same text
        String textWithShadow = PlainTextComponentSerializer.plainText().serialize(withShadow);
        String textWithoutShadow = PlainTextComponentSerializer.plainText().serialize(withoutShadow);

        assertEquals(textWithShadow, textWithoutShadow);

        assertNotEquals(withShadow, withoutShadow);
    }

    @Test
    @DisplayName("Number below minimum throws RangeException")
    void testBelowMinimum() {
        assertThrows(RangeException.class, () -> {
            renderer.renderNumber(-1, false);
        });
    }

    @Test
    @DisplayName("Number above maximum throws RangeException")
    void testAboveMaximum() {
        assertThrows(RangeException.class, () -> {
            renderer.renderNumber(10000, false);
        });
    }

    @Test
    @DisplayName("RangeException contains correct error message")
    void testRangeExceptionMessage() {
        RangeException exception = assertThrows(RangeException.class, () -> {
            renderer.renderNumber(10000, false);
        });

        assertTrue(exception.getMessage().contains("10000"));
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    @DisplayName("Boundary values (0 and 9999) work")
    void testBoundaryValues() {
        assertDoesNotThrow(() -> renderer.renderNumber(0, false));
        assertDoesNotThrow(() -> renderer.renderNumber(9999, false));
    }

    @Test
    @DisplayName("Multiple calls produce consistent results")
    void testConsistency() {
        Component first = renderer.renderNumber(777, false);
        Component second = renderer.renderNumber(777, false);

        String firstText = PlainTextComponentSerializer.plainText().serialize(first);
        String secondText = PlainTextComponentSerializer.plainText().serialize(second);

        assertEquals(firstText, secondText);
    }
}

