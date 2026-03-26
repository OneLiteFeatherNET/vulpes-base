package net.onelitefeather.vulpes.font;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;

import java.util.List;

/**
 * Represents a single font provider entry in a Minecraft resource pack font definition.
 * <p>
 * Each instance describes how a set of characters is rendered using a specific font provider
 * (e.g. bitmap or unicode) as defined in a {@code font.json}.
 *
 * @param file   the resource location of the font texture (e.g. "namespace:path/to/file.png")
 * @param type   the type of the font provider (e.g. "bitmap" or "unicode")
 * @param ascent the ascent of the glyphs, defining the distance from the baseline to the top
 * @param height the default height of the glyphs
 * @param chars  the characters mapped to this provider, typically defining the glyph layout
 */
public record FontSymbol(
        String file,
        String type,
        int ascent,
        int height,
        List<String> chars
) {

    /**
     * Codec for serializing and deserializing {@link FontSymbol} instances to and from JSON,
     * following the structure defined in Minecraft's font provider format.
     */
    public static final Codec<FontSymbol> CODEC = StructCodec.struct(
            "file", Codec.STRING, FontSymbol::file,
            "type", Codec.STRING, FontSymbol::type,
            "ascent", Codec.INT, FontSymbol::ascent,
            "height", Codec.INT, FontSymbol::height,
            "chars", Codec.STRING.list(), FontSymbol::chars,
            FontSymbol::new
    );
}
