package net.onelitefeather.vulpes.font;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;

/**
 * A custom font symbol which is defined in code and registered through {@link net.onelitefeather.vulpes.VulpesContent}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomFontSymbol extends VulpesKey permits CustomFontSymbolImpl {

    /**
     * Creates a custom font symbol which can be passed to {@link net.onelitefeather.vulpes.VulpesContent}.
     *
     * @param key    the key of the symbol
     * @param font   the key of the font the glyphs live in
     * @param symbol the font provider definition
     * @return the created symbol
     * @throws NullPointerException     if one of the arguments is null
     * @throws IllegalArgumentException if the rows of chars are empty or blank or the ascent exceeds the height
     */
    static CustomFontSymbol of(Key key, Key font, FontSymbol symbol) {
        return new CustomFontSymbolImpl(key, font, symbol);
    }

    /**
     * Returns the key of the font the glyphs live in, to be used with {@code Component.font(Key)}.
     *
     * @return the font key
     */
    Key font();

    /**
     * Returns the font provider definition of the symbol.
     *
     * @return the font symbol
     */
    FontSymbol symbol();

    /**
     * Returns the rows of glyphs of the symbol.
     *
     * @return the rows of glyphs
     */
    default List<String> chars() {
        return symbol().chars();
    }
}
