package net.onelitefeather.vulpes.font;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpaceFontTest {

    @Test
    void testOffsetMethod() {
        assertEquals(Component.empty(), SpaceFont.offset(0));
        assertEquals(SpaceFont.negative(10), SpaceFont.offset(-10));
        assertEquals(SpaceFont.positive(10), SpaceFont.offset(10));
    }

    @Test
    void testNegativeGlyphCodepoints() {
        assertEquals("\uF001", ((net.kyori.adventure.text.TextComponent) SpaceFont.negative(1)).content());
        assertEquals("\uF00D", ((net.kyori.adventure.text.TextComponent) SpaceFont.negative(256)).content());
    }

    @Test
    void testPositiveGlyphCodepoints() {
        assertEquals("\uF00F", ((net.kyori.adventure.text.TextComponent) SpaceFont.positive(1)).content());
        assertEquals("\uF01B", ((net.kyori.adventure.text.TextComponent) SpaceFont.positive(256)).content());
    }
}
