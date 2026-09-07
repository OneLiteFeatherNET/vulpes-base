package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NegativeSpaceFontTest {

    @ParameterizedTest(name = "Test simple negative space for {0} pixels")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 16, 32, 64, 128, 256})
    void testSimpleNegativeFont(int pixels) {
        assertNotNull(SpaceFont.negative(pixels));
    }

    @ParameterizedTest(name = "Test negative composite space for {0} pixels")
    @ValueSource(ints = {53, 77, 100, 255, 300})
    void testNegativeCompositeSpaces(int pixels) {
        assertNotNull(SpaceFont.negative(pixels));
    }

    @Test
    void testCorrectFont() {
        Component component = SpaceFont.negative(8);
        assertEquals(SpaceFont.FONT_KEY, component.font());
    }

    @ParameterizedTest(name = "Test invalid space input for {0}")
    @ValueSource(ints = {-1, -128})
    void testInvalidInput(int pixels) {
        assertThrows(IllegalArgumentException.class, () -> SpaceFont.negative(pixels));
    }

    @ParameterizedTest(name = "Negative offset for {0} pixels measures as -{0} via TextWidth")
    @ValueSource(ints = {1, 53, 300, 532, 600, 1000})
    void testNegativeOffsetMeasuresCorrectPixelWidth(int pixels) {
        assertEquals(-pixels, TextWidth.widthOf(SpaceFont.negative(pixels)));
    }
}
