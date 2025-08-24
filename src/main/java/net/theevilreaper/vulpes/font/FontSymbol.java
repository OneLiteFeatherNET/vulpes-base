package net.theevilreaper.vulpes.font;

import net.theevilreaper.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    @UnmodifiableView
    List<String> symbols();
}
