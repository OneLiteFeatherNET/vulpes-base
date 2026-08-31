package net.onelitefeather.vulpes.font;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;

/**
 * Wraps text in a dynamically-sized background bar built from a project's
 * own bitmap-font glyphs ({@link BarGlyphs}).
 */
public final class BackgroundBar {

    private BackgroundBar() {}

    /**
     * Wraps {@code text} in a dynamically-sized background bar built from
     * {@code glyphs}, sized to exactly cover the pixel width of {@code text}
     * plus {@code paddingPx} pixels of padding on each side.
     * <p>
     * Every bitmap-provider glyph's real rendered advance is
     * {@code actualOpaqueWidth + 1}, one pixel wider than its nominal width.
     * To keep the bar visually solid (no transparent seams) and its total
     * rendered advance exactly equal to the computed {@code barWidth}, each
     * glyph emitted by {@link #buildBarGlyphs(int, BarGlyphs)} is immediately
     * followed by a 1px {@code space:default} rewind ({@link
     * SpaceFont#negative(int)}) that cancels out that glyph's extra pixel.
     *
     * @param text      the component to render on top of the bar; only its own
     *                  style is used, this method never mutates or restyles it
     * @param paddingPx pixels of padding to add on each side of {@code text}'s
     *                  measured width; must be {@code >= 0}
     * @param tint      the color applied to the bar's glyphs
     * @param glyphs    the project's bar font configuration
     * @return {@code text} wrapped with a background bar, or {@code text}
     * unchanged if its measured pixel width is {@code <= 0} (e.g. it is empty or
     * only contains non-{@link net.kyori.adventure.text.TextComponent} content)
     * @throws IllegalArgumentException if {@code paddingPx} is negative
     */
    @Contract(pure = true)
    public static Component wrap(Component text, int paddingPx, TextColor tint, BarGlyphs glyphs) {
        if (paddingPx < 0) throw new IllegalArgumentException("paddingPx must be >= 0, got " + paddingPx);

        int textWidth = TextWidth.widthOf(text);
        if (textWidth <= 0) return text;

        int barWidth = 2 * paddingPx + textWidth;

        return Component.empty()
                .append(buildBar(barWidth, tint, glyphs))
                .append(SpaceFont.negative(barWidth))
                .append(SpaceFont.positive(paddingPx))
                .append(text);
    }

    /**
     * Builds the renderable bar segment: a {@code glyphs.font()}-styled component
     * whose total rendered pixel advance (as measured by {@link
     * TextWidth#widthOf(Component)}) is exactly {@code barWidth}.
     * <p>
     * {@code buildBarGlyphs} decomposes {@code (barWidth - 1)} into power-of-two
     * glyphs from {@code glyphs.powerGlyphs()} and appends {@code
     * glyphs.endGlyph()}, whose own nominal 1px contributes the final pixel of
     * {@code barWidth} (decomposition sum + end's 1px == {@code barWidth}).
     * Every glyph's real rendered advance is {@code nominalWidth + 1}px (see
     * the bitmap-provider note on {@link #wrap(Component, int, TextColor, BarGlyphs)}),
     * so each glyph is immediately followed by a 1px {@code space:default}
     * rewind ({@link SpaceFont#negative(int)}) that cancels the extra pixel,
     * keeping the net advance per glyph equal to its nominal width.
     *
     * @param barWidth the intended total rendered pixel width of the bar
     * @param tint     the color applied to the bar's glyphs
     * @param glyphs   the project's bar font configuration
     * @return the bar segment component
     */
    @Contract(pure = true)
    static Component buildBar(int barWidth, TextColor tint, BarGlyphs glyphs) {
        Component bar = Component.empty().font(glyphs.font()).color(tint);
        for (char glyph : buildBarGlyphs(barWidth - 1, glyphs).toCharArray()) {
            bar = bar.append(Component.text(String.valueOf(glyph)))
                    .append(SpaceFont.negative(1));
        }
        return bar;
    }

    static String buildBarGlyphs(int width, BarGlyphs glyphs) {
        StringBuilder sb = new StringBuilder();
        int remaining = width;
        char[] power = glyphs.powerGlyphs();

        for (int bit = 7; bit >= 0; bit--) {
            int magnitude = 1 << bit;
            while (remaining >= magnitude) {
                sb.append(power[bit]);
                remaining -= magnitude;
            }
        }

        sb.append(glyphs.endGlyph());
        return sb.toString();
    }
}
