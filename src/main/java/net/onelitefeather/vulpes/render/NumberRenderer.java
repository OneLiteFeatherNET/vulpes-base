package net.onelitefeather.vulpes.render;

import net.kyori.adventure.text.Component;

/**
 * The {@link NumberRenderer} interface is a specialized version of the {@link FontRenderer} and is intended to be used
 * to render numerical values using custom fonts with specific character mappings for digits.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @see NumberFontRenderer
 * @since 0.1.0
 */
public interface NumberRenderer extends FontRenderer {

    /**
     * Renders the given number as a {@link Component} using the custom font and digit mappings.
     *
     * @param number the number to render
     * @param shadow whether to apply shadow effect
     * @return the rendered number as a {@link Component}
     */
    Component renderNumber(int number, boolean shadow);
}
