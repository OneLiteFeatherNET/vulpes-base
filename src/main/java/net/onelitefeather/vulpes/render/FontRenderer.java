package net.onelitefeather.vulpes.render;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * The {@link FontRenderer} interface serves as a foundational contract for rendering text components
 * using custom fonts within the Vulpes framework. It provides common functionality and constants
 * that can be utilized by various font rendering implementations.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
// TODO: Expand this interface with common font rendering methods if needed in the future.
public interface FontRenderer {

    /**
     * A {@link Component} representing text with shadow effect.
     */
    Component NO_SHADOW = MiniMessage.miniMessage().deserialize("<!shadow>");
}
