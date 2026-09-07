package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.Contract;

/**
 * Utility for building pixel-offset strings using the {@code space:default} font.
 * <p>
 * Maps to the negative-space font definition under {@code <font:space:default>}.
 * Usage: combine characters to reach any offset, similar to how you'd make
 * change with coins.
 * <p>
 * <b>Resource-pack contract:</b> this class only produces correct visual
 * offsets if the consuming project's resource pack ships a {@code space:default}
 * bitmap font whose glyphs have exactly the rendered pixel advances implied by
 * this class's codepoints — the negative-offset glyphs ({@code \u005CuF001}..{@code
 * \u005CuF00D}) advancing by {@code -1}..{@code -256}px, the positive-offset glyphs
 * ({@code \u005CuF00F}..{@code \u005CuF01B}, plus {@link #ZERO}) advancing by {@code
 * 0}..{@code 256}px, and the vanilla {@code ' '} (space) character also mapped
 * to {@code +1}px. The exact, authoritative codepoint-to-pixel-advance mapping
 * is the {@code space:default} object in {@code src/main/resources/font/font-widths.json},
 * which {@link TextWidth} also uses to measure these offsets.
 * <p>
 * There is no client-side validation of this contract: if a resource pack
 * doesn't define {@code space:default}, or defines it with different advances,
 * Minecraft does not throw or warn — it silently falls back to a
 * default/placeholder glyph advance, producing visually wrong pixel offsets
 * (e.g. misaligned background bars or text) with no error anywhere.
 */
public final class SpaceFont {

    public static final Key FONT_KEY = Key.key("space", "default");
    private static final Component RESET = Component.empty().font(Style.DEFAULT_FONT);

    private static final int[] MAGNITUDES = {256, 128, 64, 32, 16, 8, 7, 6, 5, 4, 3, 2, 1};

    private static final char BASE_NEGATIVE_CODEPOINT = '\uF001';
    public static final char ZERO                    = '\uF00E';
    private static final char BASE_POSITIVE_CODEPOINT = '\uF00F';

    private static final char[] NEGATIVE_GLYPHS = new char[MAGNITUDES.length];
    private static final char[] POSITIVE_GLYPHS = new char[MAGNITUDES.length];

    static {
        for (int i = 0; i < MAGNITUDES.length; i++) {
            int offset = MAGNITUDES.length - 1 - i;
            NEGATIVE_GLYPHS[i] = (char) (BASE_NEGATIVE_CODEPOINT + offset);
            POSITIVE_GLYPHS[i] = (char) (BASE_POSITIVE_CODEPOINT + offset);
        }
    }

    private SpaceFont() {}

    /**
     * Builds a string that shifts the cursor {@code pixels} pixels to the left (negative).
     *
     * @param pixels positive number of pixels to shift left, e.g. {@code 128}
     * @return the composed character string to embed in a Component
     */
    @Contract(pure = true)
    public static Component negative(int pixels) {
        return createComponent(pixels, true);
    }

    /**
     * Builds a string that shifts the cursor {@code pixels} pixels to the right (positive).
     *
     * @param pixels positive number of pixels to shift right, e.g. {@code 32}
     * @return the composed character string to embed in a Component
     */
    @Contract(pure = true)
    public static Component positive(int pixels) {
        return createComponent(pixels, false);
    }

    /**
     * Offset with sign: negative value → shift left, positive → shift right.
     */
    @Contract(pure = true)
    public static Component offset(int pixels) {
        if (pixels < 0) return negative(-pixels);
        if (pixels > 0) return positive(pixels);
        return Component.empty();
    }

    // --- Internal ---

    private static Component createComponent(int pixels, boolean negative) {
        return Component.text(build(pixels, negative))
                .font(FONT_KEY)
                .append(RESET);
    }

    /**
     * Converts a given amount of pixels into a char representation to shift them around
     * @param pixels the amount of pixels to shift
     * @param negative if negative or not
     * @return a string which contains the pixel data
     */
    private static String build(int pixels, boolean negative) {
        if (pixels < 0) throw new IllegalArgumentException("pixels must be >= 0, got " + pixels);

        StringBuilder sb = new StringBuilder();
        int remaining = pixels;
        char[] glyphs = negative ? NEGATIVE_GLYPHS : POSITIVE_GLYPHS;

        for (int i = 0; i < MAGNITUDES.length; i++) {
            int magnitude = MAGNITUDES[i];
            char glyph = glyphs[i];
            while (remaining >= magnitude) {
                sb.append(glyph);
                remaining -= magnitude;
            }
        }
        return sb.toString();
    }
}
