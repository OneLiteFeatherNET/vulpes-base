package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomFontSymbolImplTest {

    private static final Key KEY = Key.key("test", "hud_icons");
    private static final Key FONT = Key.key("test", "font");

    private static FontSymbol symbol(int ascent, int height, List<String> chars) {
        return new FontSymbol("test:font/hud.png", "bitmap", ascent, height, chars);
    }

    @Test
    void acceptsValidValues() {
        CustomFontSymbolImpl value = new CustomFontSymbolImpl(KEY, FONT, symbol(7, 8, List.of("ab")));

        assertEquals(FONT, value.font());
        assertEquals(List.of("ab"), value.chars(), "chars should delegate to the symbol");
    }

    @Test
    void acceptsAscentEqualToHeight() {
        assertDoesNotThrow(() -> new CustomFontSymbolImpl(KEY, FONT, symbol(8, 8, List.of("a"))),
                "An ascent equal to the height is valid");
    }

    @Test
    void rejectsAscentAboveHeightNamingKey() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CustomFontSymbolImpl(KEY, FONT, symbol(9, 8, List.of("a"))));

        assertTrue(ex.getMessage().contains("test:hud_icons"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsNonPositiveHeight() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomFontSymbolImpl(KEY, FONT, symbol(0, 0, List.of("a"))),
                "A height of zero must be rejected");
    }

    @Test
    void rejectsEmptyChars() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomFontSymbolImpl(KEY, FONT, symbol(7, 8, List.of())),
                "Symbols without rows must be rejected");
    }

    @Test
    void rejectsBlankRow() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomFontSymbolImpl(KEY, FONT, symbol(7, 8, List.of("a", " "))),
                "Blank rows must be rejected");
    }

    @Test
    void rejectsNullFont() {
        assertThrows(NullPointerException.class,
                () -> new CustomFontSymbolImpl(KEY, null, symbol(7, 8, List.of("a"))),
                "A missing font key must be rejected");
    }

    @Test
    void getThrowsForMissingSymbol() {
        assertThrows(IllegalStateException.class, () -> CustomFontSymbolImpl.get(Key.key("test", "missing")),
                "Unknown symbols must be reported");
    }

    @Test
    void valuesAreEmptyWithPlaceholderEntries() {
        assertTrue(CustomFontSymbol.values().isEmpty(), "Placeholder entries should yield no symbols");
        assertNull(CustomFontSymbol.fromKey(KEY), "Unknown key should resolve to null");
    }
}
