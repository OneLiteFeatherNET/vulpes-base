package net.onelitefeather.vulpes.font;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FontSymbolTest {

    private static final String TEST_JSON =
            """
                    {
                      "file": "manis:global/player_ranks/admin.png",
                      "type": "bitmap",
                      "ascent": 10,
                      "height": 10,
                      "chars": [
                        ""
                      ]
                    }
                    """;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    void testNameTagRead() {
        FontSymbol tag = new FontSymbol(
                "manis:global/player_ranks/admin.png",
                "bitmap",
                10,
                10,
                List.of("\uE120")
        );

        FontSymbol loadedText = GSON.fromJson(TEST_JSON, FontSymbol.class);
        assertNotNull(loadedText);

        assertEquals(tag.file(), loadedText.file());
        assertEquals(tag.type(), loadedText.type());
        assertEquals(tag.ascent(), loadedText.ascent());
        assertEquals(tag.height(), loadedText.height());
        assertEquals(1,  tag.chars().size());
        assertEquals(tag.chars(), loadedText.chars());
    }

    @Test
    void testNameTagMethod() {
        FontSymbol loadedText = GSON.fromJson(TEST_JSON, FontSymbol.class);
        assertNotNull(loadedText);

        assertTrue(loadedText.hasChars());
        assertEquals("\uE120", loadedText.getChar(0));
        assertEquals("\uE120", loadedText.chars().getFirst());

        assertNull(loadedText.getChar(1));
        assertNull(loadedText.getChar(-1));
        assertEquals("\uE000", loadedText.getCharOr(1, "\uE000"));
        assertEquals("\uE000", loadedText.getCharOr(-1, "\uE000"));
    }

    @Test
    void testEmptyChars() {
        FontSymbol tag = new FontSymbol(
                "manis:global/player_ranks/admin.png",
                "bitmap",
                10,
                10,
                List.of()
        );
        assertFalse(tag.hasChars());
    }
}
