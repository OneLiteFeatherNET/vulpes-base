package net.onelitefeather.vulpes.font;

import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

public sealed interface FontSymbol extends FontSymbols, VulpesKey permits BitFontSymbol {

    /**
     * Returns the symbol ascent.
     *
     * @return the ascent
     */
    int ascent();

    /**
     * Returns the symbol height.
     *
     * @return the height
     */
    int height();

    /**
     * Returns all symbols for the font.
     *
     * @return the symbols
     */
    @UnmodifiableView
    List<String> symbols();
}
