package net.onelitefeather.vulpes.render;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.onelitefeather.vulpes.render.annotation.NumberRange;
import net.onelitefeather.vulpes.render.exception.RangeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders numbers using a specified font by mapping digits to Unicode characters.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public class NumberFontRenderer implements NumberRenderer {

    private final Key fontKey;
    private final int min;
    private final int max;
    private final Map<Character, Character> digitMap;

    /**
     * Constructs a NumberFontRenderer using the NumberRange annotation for configuration.
     *
     * @param key the font key
     * @throws IllegalStateException if the NumberRange annotation is not present
     */
    public NumberFontRenderer(Key key) {
        this.fontKey = key;

        NumberRange numberRange = this.getClass().getAnnotation(NumberRange.class);
        if (numberRange == null) {
            throw new IllegalStateException("NumberFontRenderer requires a NumberRange annotation");
        }

        this.min = numberRange.min();
        this.max = numberRange.max();
        this.digitMap = createDigitMap(numberRange.startUnicode());
    }

    /**
     * Constructs a NumberFontRenderer with a starting Unicode for digit mapping.
     *
     * @param fontKey      the font key
     * @param min          the minimum number
     * @param max          the maximum number
     * @param startUnicode the starting Unicode code point for digit '0'
     */
    public NumberFontRenderer(Key fontKey, int min, int max, int startUnicode) {
        this.fontKey = fontKey;
        this.min = min;
        this.max = max;
        this.digitMap = createDigitMap(startUnicode);
    }

    /**
     * Constructs a NumberFontRenderer with Unicode ranges for digit mapping.
     *
     * @param fontKey       the font key
     * @param min           the minimum number
     * @param max           the maximum number
     * @param unicodeRanges the Unicode ranges for digit mapping
     */
    public NumberFontRenderer(Key fontKey, int min, int max, int[] unicodeRanges) {
        this.fontKey = fontKey;
        this.min = min;
        this.max = max;

        if (unicodeRanges.length > 0) {
            this.digitMap = createDigitMap(unicodeRanges[0]);
        } else {
            this.digitMap = new HashMap<>();
        }
    }

    /**
     * Constructs a NumberFontRenderer with a custom digit mapping.
     *
     * @param fontKey       the font key
     * @param min           the minimum number
     * @param max           the maximum number
     * @param customMapping the custom digit to Unicode character mapping
     */
    public NumberFontRenderer(Key fontKey, int min, int max, Map<Character, Character> customMapping) {
        this.fontKey = fontKey;
        this.min = min;
        this.max = max;
        this.digitMap = new HashMap<>(customMapping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component renderNumber(int number, boolean shadow) {
        this.validateNumber(number);
        String numberStr = String.valueOf(number);
        StringBuilder result = new StringBuilder();
        for (char currentChar : numberStr.toCharArray()) {
            Character unicode = digitMap.get(currentChar);
            if (unicode != null) {
                result.append(unicode);
            } else {
                result.append(currentChar);
            }
        }
        Component component = Component.text(result.toString()).font(fontKey);
        return shadow ? NO_SHADOW.append(component) : component;
    }

    /**
     * Validates that the given number is within the defined range.
     *
     * @param number the number to validate
     * @throws RangeException if the number is out of range
     */
    private void validateNumber(int number) {
        if (number < min || number > max) {
            throw new RangeException("Number " + number + " is out of range [" + min + ", " + max + "]");
        }
    }

    /**
     * Creates a mapping of digits to their corresponding Unicode characters starting from the given Unicode code point.
     *
     * @param startUnicode the starting Unicode code point for digit '0'
     * @return a map of digits to Unicode characters
     */
    private Map<Character, Character> createDigitMap(int startUnicode) {
        int mapSize = this.max - this.min;
        Map<Character, Character> map = HashMap.newHashMap(mapSize);
        for (int i = 0; i <= 9; i++) {
            char digit = (char) ('0' + i);
            char unicode = (char) (startUnicode + i);
            map.put(digit, unicode);
        }
        return map;
    }
}