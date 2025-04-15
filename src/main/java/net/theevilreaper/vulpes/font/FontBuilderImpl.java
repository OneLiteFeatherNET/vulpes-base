package net.theevilreaper.vulpes.font;

import net.theevilreaper.vulpes.exception.FontBuilderException;
import org.jetbrains.annotations.NotNull;

public final class FontBuilderImpl implements FontSymbol.Builder {

    private char[] symbols;
    private double[] shift;
    private int height;
    private int ascent;

    @Override
    public FontSymbol.@NotNull Builder symbols(char[] symbols) {

        this.symbols = symbols;
        return this;
    }

    @Override
    public FontSymbol.@NotNull Builder shift(double[] shift) {
        this.shift = shift;
        return this;
    }

    @Override
    public FontSymbol.@NotNull Builder height(int height) {
        this.height = height;
        return this;
    }

    @Override
    public FontSymbol.@NotNull Builder ascent(int ascent) {
        this.ascent = ascent;
        return this;
    }

    @Override
    public @NotNull FontSymbol build() {
        if (symbols == null || symbols.length == 0) {
            throw new FontBuilderException("The symbols array can't be null or empty");
        }
        return new FontSymbol(symbols, shift, height, ascent);
    }
}
