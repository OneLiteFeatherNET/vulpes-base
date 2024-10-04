package net.theevilreaper.vulpes.font;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Holds some data values for a custom font symbol
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class FontSymbol {

    private final char[] symbols;
    private final double[] shift;
    private final int height;
    private final int ascent;

    /**
     * Returns a new instance from the {@link Builder} to create font objects.
     * @return the created instance
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull Builder builder() {
        return new FontBuilderImpl();
    }

    /**
     * Creates a new reference from the {@link FontSymbol}.
     * @param symbols the symbols for the custom font symbol
     * @param shift
     * @param height
     * @param ascent
     */
    protected FontSymbol(char[] symbols, double[] shift, int height, int ascent) {
        this.symbols = symbols;
        this.shift = shift;
        this.height = height;
        this.ascent = ascent;
    }

    public char[] getSymbols() {
        return symbols;
    }

    public double[] getShift() {
        return shift;
    }

    public int getHeight() {
        return height;
    }

    public int getAscent() {
        return ascent;
    }

    sealed interface Builder permits FontBuilderImpl {

        @Contract(value = "_ -> this", pure = true)
        @NotNull Builder symbols(char[] symbols);

        @Contract(value = "_ -> this", pure = true)
        @NotNull Builder shift(double[] shift);

        @Contract(value = "_ -> this", pure = true)
        @NotNull Builder height(int height);

        @Contract(value = "_ -> this", pure = true)
        @NotNull Builder ascent(int ascent);

        /**
         * Creates a new object from the {@link FontSymbol}.
         * @return the created instance
         */
        @Contract(pure = true)
        @NotNull FontSymbol build();
    }
}
