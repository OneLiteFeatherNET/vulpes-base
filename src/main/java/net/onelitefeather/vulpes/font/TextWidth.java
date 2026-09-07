package net.onelitefeather.vulpes.font;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Measures the pixel width of Adventure {@link Component} trees against a
 * per-font table of codepoint pixel widths.
 * <p>
 * Ships with widths for {@code minecraft:default} and {@code space:default}
 * only (project-independent, vanilla/convention data). Any other font
 * (custom icon fonts, project-specific bar fonts) must be registered by the
 * consuming project via {@link #registerFont(Key, Map)} before it is
 * measured, otherwise unknown codepoints fall back to {@link #FALLBACK_WIDTH}.
 */
public final class TextWidth {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextWidth.class);
    private static final Key DEFAULT_FONT = Key.key("minecraft", "default");
    private static final int FALLBACK_WIDTH = 6;

    private static final Map<Key, Map<Integer, Integer>> WIDTHS = new ConcurrentHashMap<>(load());
    private static final Set<String> WARNED = ConcurrentHashMap.newKeySet();

    private TextWidth() {}

    /**
     * Registers (or fully replaces) the pixel-width table for {@code font}.
     * <p>
     * Replacement is total: calling this again for the same {@code font} key
     * discards the previously registered table for that key rather than
     * merging individual codepoints into it.
     *
     * @param font   the font key to register widths for
     * @param widths codepoint (as {@link Integer}) to pixel-width map
     */
    public static void registerFont(Key font, Map<Integer, Integer> widths) {
        WIDTHS.put(font, Map.copyOf(widths));
    }

    /**
     * Measures the pixel width of {@code component}, recursing into its children
     * and resolving each child's font from its own explicit {@link
     * net.kyori.adventure.text.format.Style#font()} or, if unset, from the
     * nearest ancestor's font (defaulting to {@code minecraft:default} at the
     * root).
     * <p>
     * Only {@link TextComponent} content is measured, character by character,
     * against the registered font-widths tables. Other component types (e.g.
     * {@code TranslatableComponent}) contribute {@code 0} width for their own
     * content, since translation keys are not resolved to their translated text.
     * <p>
     * This method does not account for {@code bold} (+1px per character in
     * vanilla Minecraft) or {@code italic} styling; widths are measured as if
     * the text were rendered in the regular style.
     *
     * @param component the component to measure
     * @return the total measured pixel width
     */
    public static int widthOf(Component component) {
        return widthOf(component, DEFAULT_FONT);
    }

    private static int widthOf(Component component, Key inheritedFont) {
        Key font = component.style().font() != null ? component.style().font() : inheritedFont;
        int width = 0;

        if (component instanceof TextComponent text) {
            String content = text.content();
            for (int i = 0; i < content.length(); i++) {
                width += widthOfChar(content.charAt(i), font);
            }
        }

        for (Component child : component.children()) {
            width += widthOf(child, font);
        }

        return width;
    }

    private static int widthOfChar(char c, Key font) {
        Map<Integer, Integer> fontWidths = WIDTHS.get(font);
        Integer width = fontWidths != null ? fontWidths.get((int) c) : null;

        if (width == null) {
            if (WARNED.add(font.asString() + '#' + (int) c)) {
                LOGGER.warn("No width entry for codepoint {} (U+{}) in font {}, using fallback width {}",
                        (int) c, Integer.toHexString(c), font.asString(), FALLBACK_WIDTH);
            }
            return FALLBACK_WIDTH;
        }

        return width;
    }

    private static Map<Key, Map<Integer, Integer>> load() {
        InputStream in = TextWidth.class.getResourceAsStream("/font/font-widths.json");
        if (in == null) {
            throw new IllegalStateException("Missing classpath resource /font/font-widths.json");
        }

        Map<String, Map<Integer, Integer>> raw;
        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            raw = new Gson().fromJson(reader, new TypeToken<Map<String, Map<Integer, Integer>>>() {}.getType());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load font-widths.json", e);
        }

        Map<Key, Map<Integer, Integer>> result = new HashMap<>();
        for (Map.Entry<String, Map<Integer, Integer>> entry : raw.entrySet()) {
            result.put(Key.key(entry.getKey()), entry.getValue());
        }
        return result;
    }
}
