package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BarGlyphsTest {

    private static final Key FONT = Key.key("vulpes", "test_bar");
    private static final char END_GLYPH = '\uE100';
    private static final char[] VALID_GLYPHS = {
            '\uE001', '\uE002', '\uE004', '\uE008',
            '\uE010', '\uE020', '\uE040', '\uE080'
    };
    private static final char MUTATED_GLYPH = '\uE0FF';

    @Test
    void testValidPowerGlyphsAreStored() {
        BarGlyphs barGlyphs = new BarGlyphs(FONT, VALID_GLYPHS, END_GLYPH);

        assertEquals(FONT, barGlyphs.font());
        assertEquals(END_GLYPH, barGlyphs.endGlyph());
        assertEquals(BarGlyphs.POWER_GLYPH_COUNT, barGlyphs.powerGlyphs().length);
    }

    @Test
    void testTooFewPowerGlyphsThrows() {
        char[] tooShort = {'\uE001', '\uE002'};
        assertThrows(IllegalArgumentException.class, () -> new BarGlyphs(FONT, tooShort, END_GLYPH));
    }

    @Test
    void testTooManyPowerGlyphsThrows() {
        char[] tooLong = {
                '\uE001', '\uE002', '\uE004', '\uE008',
                '\uE010', '\uE020', '\uE040', '\uE080', '\uE0FF'
        };
        assertThrows(IllegalArgumentException.class, () -> new BarGlyphs(FONT, tooLong, END_GLYPH));
    }

    @Test
    void testEmptyPowerGlyphsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new BarGlyphs(FONT, new char[0], END_GLYPH));
    }

    @Test
    void testMutatingConstructorArrayDoesNotAffectRecord() {
        char[] source = VALID_GLYPHS.clone();
        BarGlyphs barGlyphs = new BarGlyphs(FONT, source, END_GLYPH);

        source[0] = MUTATED_GLYPH;

        assertEquals(VALID_GLYPHS[0], barGlyphs.powerGlyphs()[0]);
    }

    @Test
    void testMutatingReturnedArrayDoesNotAffectRecord() {
        BarGlyphs barGlyphs = new BarGlyphs(FONT, VALID_GLYPHS.clone(), END_GLYPH);

        char[] returned = barGlyphs.powerGlyphs();
        returned[0] = MUTATED_GLYPH;

        assertEquals(VALID_GLYPHS[0], barGlyphs.powerGlyphs()[0]);
    }

    @Test
    void testEqualBarGlyphsFromSeparateArraysAreEqual() {
        BarGlyphs first = new BarGlyphs(FONT, VALID_GLYPHS.clone(), END_GLYPH);
        BarGlyphs second = new BarGlyphs(FONT, VALID_GLYPHS.clone(), END_GLYPH);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}
