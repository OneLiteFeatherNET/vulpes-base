package net.onelitefeather.vulpes.render.number;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.onelitefeather.vulpes.render.NumberFontRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleFileNumberRenderTest {

    private NumberFontRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new NumberFontRenderer(
                Key.key("test", "single"),
                0,
                99,
                0xE120
        );
    }

    @Test
    @DisplayName("Single range works correctly")
    void testSingleRange() {
        Component result = renderer.renderNumber(8, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);
        assertEquals("\uE128", text);
    }

    @Test
    @DisplayName("Two-digit number with single range")
    void testTwoDigitsSingleRange() {
        Component result = renderer.renderNumber(91, false);
        String text = PlainTextComponentSerializer.plainText().serialize(result);
        assertEquals("\uE129\uE121", text);
    }
}
