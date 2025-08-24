package net.theevilreaper.vulpes.font;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FontSymbolTest {

    @Test
    void testFontCreationWithoutFactory() {
        Key key = Key.key("vulpes", "test");
        FontSymbol fontSymbol = new BitFontSymbol(key, 10, 20, List.of("\u12ca"));
        assertNotNull(fontSymbol);
        assertInstanceOf(BitFontSymbol.class, fontSymbol);

        assertEquals(key, fontSymbol.key());
        assertEquals(10, fontSymbol.ascent());
        assertEquals(20, fontSymbol.height());
        assertEquals(1, fontSymbol.symbols().size());
        assertEquals(List.of("\u12ca"), fontSymbol.symbols());
    }
}
