package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BackgroundBarTest {

    private static final Key BAR_FONT = Key.key("vulpes", "test_bar");
    private static final char GLYPH_1   = '\uE001';
    private static final char GLYPH_2   = '\uE002';
    private static final char GLYPH_4   = '\uE004';
    private static final char GLYPH_8   = '\uE008';
    private static final char GLYPH_16  = '\uE010';
    private static final char GLYPH_32  = '\uE020';
    private static final char GLYPH_64  = '\uE040';
    private static final char GLYPH_128 = '\uE080';
    private static final char GLYPH_END = '\uE100';

    private static final BarGlyphs GLYPHS = new BarGlyphs(
            BAR_FONT,
            new char[] {GLYPH_1, GLYPH_2, GLYPH_4, GLYPH_8, GLYPH_16, GLYPH_32, GLYPH_64, GLYPH_128},
            GLYPH_END
    );

    @BeforeAll
    static void registerBarFontWidths() {
        // Mirrors a real bitmap-provider font: every glyph's rendered advance
        // is its nominal width + 1px (see BackgroundBar.buildBar javadoc).
        TextWidth.registerFont(BAR_FONT, Map.ofEntries(
                Map.entry((int) GLYPH_1, 2),
                Map.entry((int) GLYPH_2, 3),
                Map.entry((int) GLYPH_4, 5),
                Map.entry((int) GLYPH_8, 9),
                Map.entry((int) GLYPH_16, 17),
                Map.entry((int) GLYPH_32, 33),
                Map.entry((int) GLYPH_64, 65),
                Map.entry((int) GLYPH_128, 129),
                Map.entry((int) GLYPH_END, 2)
        ));
    }

    @Test
    void testBarSegmentUsesGivenFont() {
        Component wrapped = BackgroundBar.wrap(Component.text("A"), 4, NamedTextColor.BLACK, GLYPHS);
        Component barSegment = wrapped.children().get(0);
        assertEquals(BAR_FONT, barSegment.font());
    }

    @Test
    void testBarSegmentUsesGivenTint() {
        Component wrapped = BackgroundBar.wrap(Component.text("A"), 4, NamedTextColor.RED, GLYPHS);
        Component barSegment = wrapped.children().get(0);
        assertEquals(NamedTextColor.RED, barSegment.color());
    }

    @Test
    void testWrappedRootHasNoInheritableFontOrColor() {
        Component wrapped = BackgroundBar.wrap(Component.text("A"), 4, NamedTextColor.RED, GLYPHS);
        assertEquals(null, wrapped.font());
        assertEquals(null, wrapped.color());
    }

    @Test
    void testTextWithOwnStyleIsNotOverriddenByBarStyle() {
        Key customFont = Key.key("minecraft", "default");
        Component styledText = Component.text("Boss")
                .font(customFont)
                .color(NamedTextColor.WHITE);

        Component wrapped = BackgroundBar.wrap(styledText, 4, NamedTextColor.RED, GLYPHS);

        Component textSegment = wrapped.children().get(wrapped.children().size() - 1);
        assertEquals(customFont, textSegment.font());
        assertEquals(NamedTextColor.WHITE, textSegment.color());
    }

    @Test
    void testPlainUnstyledTextIsNotMutatedByWrap() {
        Component plainText = Component.text("Boss");

        Component wrapped = BackgroundBar.wrap(plainText, 4, NamedTextColor.RED, GLYPHS);

        Component textSegment = wrapped.children().get(wrapped.children().size() - 1);
        assertEquals(plainText, textSegment);
        assertEquals(null, textSegment.font());
        assertEquals(null, textSegment.color());
    }

    @Test
    void testEmptyTextReturnsUnchanged() {
        Component empty = Component.empty();
        assertEquals(empty, BackgroundBar.wrap(empty, 4, NamedTextColor.BLACK, GLYPHS));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    void testNegativePaddingThrows(int padding) {
        assertThrows(IllegalArgumentException.class,
                () -> BackgroundBar.wrap(Component.text("A"), padding, NamedTextColor.BLACK, GLYPHS));
    }

    @Test
    void testBuildBarGlyphsDecomposesBinary() {
        // 13 = 8 + 4 + 1
        String glyphs = BackgroundBar.buildBarGlyphs(13, GLYPHS);
        assertEquals("" + GLYPH_8 + GLYPH_4 + GLYPH_1 + GLYPH_END, glyphs);
    }

    @Test
    void testBuildBarGlyphsRepeats128ForWideBars() {
        // 300 = 128 + 128 + 32 + 8 + 4
        String glyphs = BackgroundBar.buildBarGlyphs(300, GLYPHS);
        assertEquals("" + GLYPH_128 + GLYPH_128 + GLYPH_32 + GLYPH_8 + GLYPH_4 + GLYPH_END, glyphs);
    }

    /**
     * Invariant required by the design spec: the bar segment's actual rendered
     * pixel advance (as measured by {@link TextWidth#widthOf(Component)}, the
     * same measurement used at runtime) must equal the intended {@code barWidth}
     * exactly, even though every registered glyph's real advance is
     * {@code nominalWidth + 1}px (see {@link #registerBarFontWidths()}).
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 13, 127, 128, 129, 300, 1000})
    void testBarSegmentRenderedWidthMatchesIntendedBarWidth(int barWidth) {
        Component bar = BackgroundBar.buildBar(barWidth, NamedTextColor.BLACK, GLYPHS);
        assertEquals(barWidth, TextWidth.widthOf(bar));
    }

    @Test
    void testWrapBarSegmentRenderedWidthMatchesComputedBarWidth() {
        Component text = Component.text("Boss Fight");
        int paddingPx = 5;
        int expectedBarWidth = 2 * paddingPx + TextWidth.widthOf(text);

        Component wrapped = BackgroundBar.wrap(text, paddingPx, NamedTextColor.BLACK, GLYPHS);
        Component barSegment = wrapped.children().get(0);

        assertEquals(expectedBarWidth, TextWidth.widthOf(barSegment));
    }

    /**
     * Regression coverage for wide bars (barWidth &gt; 532px) going through the
     * real {@link BackgroundBar#wrap} path: the net cursor advance of the
     * fully wrapped component (bar + rewind + padding + text) must equal
     * {@code paddingPx + TextWidth.widthOf(text)} exactly, i.e. the trailing
     * text ends up exactly one padding's width after where the bar started.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 100, 532, 600, 1000})
    void testWrapNetCursorAdvanceEqualsPaddingPlusTextWidth(int paddingPx) {
        Component text = Component.text("Boss Fight");
        Component wrapped = BackgroundBar.wrap(text, paddingPx, NamedTextColor.BLACK, GLYPHS);
        assertEquals(paddingPx + TextWidth.widthOf(text), TextWidth.widthOf(wrapped));
    }
}