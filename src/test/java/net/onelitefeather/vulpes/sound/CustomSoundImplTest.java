package net.onelitefeather.vulpes.sound;

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomSoundImplTest {

    private static final Key KEY = Key.key("test", "ui.click");

    @Test
    void acceptsSoundWithoutRange() {
        CustomSoundImpl sound = new CustomSoundImpl(KEY, null);

        assertNull(sound.range(), "A sound without range should keep null");
    }

    @Test
    void acceptsPositiveRange() {
        assertEquals(16.0f, new CustomSoundImpl(KEY, 16.0f).range());
    }

    @Test
    void ofKeepsRangeOfSound() {
        assertEquals(16.0f, CustomSound.of(KEY, 16.0f).range(), "The factory should keep the range");
    }

    @Test
    void rejectsZeroRange() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CustomSoundImpl(KEY, 0.0f));

        assertTrue(ex.getMessage().contains("test:ui.click"), "Message should name the key: " + ex.getMessage());
    }

    @Test
    void rejectsNegativeRange() {
        assertThrows(IllegalArgumentException.class, () -> new CustomSoundImpl(KEY, -1.0f),
                "Negative ranges must be rejected");
    }

    @Test
    void rejectsNaNRange() {
        assertThrows(IllegalArgumentException.class, () -> new CustomSoundImpl(KEY, Float.NaN),
                "NaN must be rejected");
    }

    @Test
    void rejectsInfiniteRange() {
        assertThrows(IllegalArgumentException.class, () -> new CustomSoundImpl(KEY, Float.POSITIVE_INFINITY),
                "Infinity must be rejected");
    }

    @Test
    void rejectsNullKey() {
        assertThrows(NullPointerException.class, () -> new CustomSoundImpl(null, null),
                "A null key must be rejected");
    }

    @Test
    void soundEventUsesKeyAsName() {
        var event = new CustomSoundImpl(KEY, 8.0f).soundEvent();

        assertEquals("test:ui.click", event.name(), "The event should use the key as name");
        assertEquals(KEY, event.key(), "The event should use the key");
    }

    @Test
    void soundEventWithoutRangeIsCreated() {
        assertNotNull(new CustomSoundImpl(KEY, null).soundEvent(), "A sound event is expected without range");
    }
}
