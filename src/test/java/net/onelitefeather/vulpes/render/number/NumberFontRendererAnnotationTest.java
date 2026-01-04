package net.onelitefeather.vulpes.render.number;

import net.kyori.adventure.key.Key;
import net.onelitefeather.vulpes.render.NumberFontRenderer;
import net.onelitefeather.vulpes.render.annotation.NumberRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberFontRendererAnnotationTest {

    private static final Key FONT_KEY = Key.key("test", "font");

    @Test
    void testFontRendererClassWhichContainsAnnotation() {
        NumberFontRenderer renderer =
                new AnnotatedNumberFontRenderer(FONT_KEY);

        // valid value -> no exception
        assertDoesNotThrow(() -> renderer.renderNumber(0, false));
        assertDoesNotThrow(() -> renderer.renderNumber(999, false));

        // out of range
        assertThrows(
                RuntimeException.class,
                () -> renderer.renderNumber(1000, false)
        );
    }

    @Test
    void testFontRendererClassWhichContainsNoAnnotation() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new NonAnnotatedNumberFontRenderer(FONT_KEY)
        );

        assertEquals(
                "NumberFontRenderer requires a NumberRange annotation",
                exception.getMessage()
        );
    }


    @NumberRange(max = 999, startUnicode = 0xE000)
    static class AnnotatedNumberFontRenderer extends NumberFontRenderer {

        AnnotatedNumberFontRenderer(Key key) {
            super(key);
        }
    }

    static class NonAnnotatedNumberFontRenderer extends NumberFontRenderer {

        NonAnnotatedNumberFontRenderer(Key key) {
            super(key);
        }
    }

}
