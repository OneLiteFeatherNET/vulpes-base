package net.onelitefeather.vulpes.font.format;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import java.util.List;

public final class StandardCustomNumberFormatter implements CustomNumberFormat {

    private static final Key DEFAULT_FONT = Key.key("minecraft", "default");

    private final Key fontKey;
    private final List<Character> glyphs;

    /**
     * Creates a new instance of the {@link StandardCustomNumberFormatter} with the given font and glyphs.
     *
     * @param key    the font key
     * @param glyphs the glyphs for the font
     */
    protected StandardCustomNumberFormatter(Key key, List<Character> glyphs) {
        this.fontKey = key;
        this.glyphs = glyphs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component format(int number) {
        return Component.empty()
                .font(fontKey)
                .append(formatDigits(number))
                .append(Component.empty().font(DEFAULT_FONT)); // Reset after
    }

    /**
     * Converts the given number into a string with custom chars.
     *
     * @param number the number to convert
     * @return the created input as a {@link Component}
     */
    private Component formatDigits(int number) {
        if (number < 0) {
            return Component.text("-")  // No font needed, inherits from parent
                    .append(formatDigits(-number));
        }

        if (number < 10) {
            return Component.text(glyphs.get(number));
        }

        return formatDigits(number / 10)
                .append(Component.text(glyphs.get(number % 10)));
    }
}
