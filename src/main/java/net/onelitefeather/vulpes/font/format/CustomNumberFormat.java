package net.onelitefeather.vulpes.font.format;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * The {@link CustomNumberFormat} interface defines the contract for custom number formatters which based on a Minecraft Resource-Pack.
 * Each custom format requires a {@link Key} which defines the namespace of the font and a list of glyphs.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public sealed interface CustomNumberFormat permits StandardCustomNumberFormatter {

    /**
     * Creates a new custom number formatter.
     *
     * @param font   the font key
     * @param glyphs the glyphs for the font
     * @return the new custom number formatter
     */
    @Contract(pure = true, value = "_, _ -> new")
    static CustomNumberFormat of(Key font, List<Character> glyphs) {
        return new StandardCustomNumberFormatter(font, glyphs);
    }

    /**
     * Formats a given number into a {@link Component}.
     *
     * @param number the number to format
     * @return the formatted number as {@link Component}
     */
    Component format(int number);
}
