package net.onelitefeather.vulpes.font;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpaceFontTest {

    @Test
    void testOffsetMethod() {
        assertEquals(Component.empty(), SpaceFont.offset(0));
    }
}
