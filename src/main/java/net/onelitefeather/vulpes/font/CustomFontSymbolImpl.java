package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

/**
 * Default implementation of {@link CustomFontSymbol}.
 *
 * @param key    the key of the symbol
 * @param font   the key of the font the glyphs live in
 * @param symbol the font provider definition
 */
@ApiStatus.Internal
record CustomFontSymbolImpl(Key key, Key font, FontSymbol symbol) implements CustomFontSymbol {

    CustomFontSymbolImpl {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(font, "font of font symbol " + key.asString());
        Objects.requireNonNull(symbol, "symbol of font symbol " + key.asString());
        Objects.requireNonNull(symbol.chars(), "chars of font symbol " + key.asString());
        if (symbol.chars().isEmpty()) {
            throw new IllegalArgumentException("The font symbol " + key.asString() + " must have at least one row of chars");
        }
        for (String row : symbol.chars()) {
            if (row == null || row.isBlank()) {
                throw new IllegalArgumentException("The font symbol " + key.asString() + " must not have a blank row of chars");
            }
        }
        if (symbol.height() <= 0) {
            throw new IllegalArgumentException("The height " + symbol.height() + " of font symbol "
                    + key.asString() + " must be positive");
        }
        if (symbol.ascent() > symbol.height()) {
            throw new IllegalArgumentException("The ascent " + symbol.ascent() + " of font symbol "
                    + key.asString() + " exceeds the height " + symbol.height());
        }
    }
}
