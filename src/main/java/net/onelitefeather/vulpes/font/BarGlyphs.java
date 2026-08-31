package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;

import java.util.Arrays;
import java.util.Objects;

/**
 * Describes a project's background-bar bitmap font: the font key and the
 * glyphs used to greedily compose an arbitrary pixel width.
 * <p>
 * {@code powerGlyphs[i]} is the glyph for {@code 2^i} pixels
 * ({@code i} in {@code 0..7}, i.e. 1/2/4/8/16/32/64/128px). {@code endGlyph}
 * is the closing cap glyph, appended after the power-of-two decomposition.
 * <p>
 * {@code powerGlyphs} is defensively copied: the compact constructor clones
 * the array passed in, and the {@link #powerGlyphs()} accessor returns a
 * fresh clone on every call, so neither the caller's original array nor the
 * record's internal state can be mutated through the other. {@link
 * #equals(Object)} and {@link #hashCode()} are likewise overridden to compare
 * {@code powerGlyphs} by content rather than array identity.
 *
 * @param font         the font key of the bar's bitmap font
 * @param powerGlyphs  exactly 8 glyphs, indexed by power of two (2^0..2^7)
 * @param endGlyph     the closing cap glyph
 */
public record BarGlyphs(Key font, char[] powerGlyphs, char endGlyph) {

    public BarGlyphs {
        if (powerGlyphs.length != 8) {
            throw new IllegalArgumentException(
                    "powerGlyphs must have exactly 8 entries (2^0..2^7), got " + powerGlyphs.length);
        }
        powerGlyphs = powerGlyphs.clone();
    }

    /**
     * Returns a fresh clone of the stored power-of-two glyphs on every call,
     * so mutating the returned array cannot affect this record's internal state.
     *
     * @return a copy of the 8 power-of-two glyphs
     */
    @Override
    public char[] powerGlyphs() {
        return powerGlyphs.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarGlyphs other)) return false;
        return Objects.equals(font, other.font)
                && Arrays.equals(powerGlyphs, other.powerGlyphs)
                && endGlyph == other.endGlyph;
    }

    @Override
    public int hashCode() {
        return Objects.hash(font, Arrays.hashCode(powerGlyphs), endGlyph);
    }
}
