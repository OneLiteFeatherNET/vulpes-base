package net.onelitefeather.vulpes.font;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.key.Key;
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

    @Test
    void testNameTagRead() {
        FontSymbol tag = new FontSymbol(
                "manis:global/player_ranks/admin.png",
                "bitmap",
                10,
                10,
                List.of("\uE120")
        );

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FontSymbol loadedText = gson.fromJson(TEST_JSON, FontSymbol.class);
        assertNotNull(loadedText);

        assertEquals(tag.file(), loadedText.file());
        assertEquals(tag.type(), loadedText.type());
        assertEquals(tag.ascent(), loadedText.ascent());
        assertEquals(tag.height(), loadedText.height());
        assertEquals(1,  tag.chars().size());
        assertEquals(tag.chars(), loadedText.chars());
    }
}
