package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextWidthTest {

    private static final Key TEST_ICON_FONT = Key.key("vulpes", "test_icons");
    private static final char ICON_GLYPH = '\uE500';

    @BeforeAll
    static void registerTestFont() {
        TextWidth.registerFont(TEST_ICON_FONT, Map.of((int) ICON_GLYPH, 9));
    }

    @Test
    void testSingleCharacterWidth() {
        assertEquals(6, TextWidth.widthOf(Component.text("A")));
    }

    @Test
    void testSpaceWidthMatchesBundledVanillaTable() {
        assertEquals(4, TextWidth.widthOf(Component.text(" ")));
    }

    @Test
    void testMultiCharacterWidthSumsPerCharacter() {
        assertEquals(TextWidth.widthOf(Component.text("A")) * 2, TextWidth.widthOf(Component.text("AA")));
    }

    @Test
    void testMixedFontComponentTree() {
        Component mixed = Component.text("A")
                .append(Component.text(String.valueOf(ICON_GLYPH)).font(TEST_ICON_FONT));

        assertEquals(6 + 9, TextWidth.widthOf(mixed));
    }

    @Test
    void testEmptyComponentHasZeroWidth() {
        assertEquals(0, TextWidth.widthOf(Component.empty()));
    }

    @Test
    void testDeeplyNestedFontInheritance() {
        // A grandchild without its own font inherits from its parent, not the root.
        Component tree = Component.text("A") // default font (6px)
                .append(
                        Component.text(String.valueOf(ICON_GLYPH)) // test icon font (9px)
                                .font(TEST_ICON_FONT)
                                .append(Component.text(String.valueOf(ICON_GLYPH))) // inherits test icon font (9px)
                );

        assertEquals(6 + 9 + 9, TextWidth.widthOf(tree));
    }

    @Test
    void testUnknownFontFallsBackToDefaultWidth() {
        Key unregisteredFont = Key.key("vulpes", "does_not_exist");
        assertEquals(6, TextWidth.widthOf(Component.text("A").font(unregisteredFont)));
    }

    @Test
    void testRegisterFontReplacesPreviousTableForSameKey() {
        Key font = Key.key("vulpes", "replace_test");
        TextWidth.registerFont(font, Map.of((int) 'x', 1));
        TextWidth.registerFont(font, Map.of((int) 'x', 42));

        assertEquals(42, TextWidth.widthOf(Component.text("x").font(font)));
    }
}
