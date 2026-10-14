package net.onelitefeather.vulpes.sound;

import net.kyori.adventure.key.Key;
import net.minestom.server.sound.SoundEvent;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.RegistryFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Default implementation of {@link CustomSound}.
 *
 * @param key   the key of the sound
 * @param range the fixed range of the sound or null for the default attenuation
 */
@ApiStatus.Internal
record CustomSoundImpl(Key key, @Nullable Float range) implements CustomSound {

    static final Registry<CustomSound> REGISTRY =
            RegistryFactory.of(Key.key("vulpes", "sound"), CustomSoundEntries.entries());

    CustomSoundImpl {
        Objects.requireNonNull(key, "key");
        if (range != null && (!Float.isFinite(range) || range <= 0)) {
            throw new IllegalArgumentException("The range of sound " + key.asString()
                    + " must be finite and greater than zero but was " + range);
        }
    }

    @Override
    public SoundEvent soundEvent() {
        // SoundEvent.of returns an immutable value, so a new one per call is safe and equal to any other.
        return SoundEvent.of(key.asString(), range);
    }

    /**
     * Returns the sound for the given key.
     *
     * @param key the key to look up
     * @return the sound
     * @throws IllegalStateException if the sound is not registered
     */
    static CustomSound get(Key key) {
        CustomSound sound = REGISTRY.get(key);
        if (sound == null) {
            throw new IllegalStateException("Missing custom sound " + key.asString());
        }
        return sound;
    }
}
