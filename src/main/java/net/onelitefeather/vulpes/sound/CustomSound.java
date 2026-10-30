package net.onelitefeather.vulpes.sound;

import net.kyori.adventure.key.Key;
import net.minestom.server.sound.SoundEvent;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A custom sound which is defined in code and registered through {@link net.onelitefeather.vulpes.VulpesContent}.
 *
 * @since 0.5.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomSound extends VulpesKey permits CustomSoundImpl {

    /**
     * Creates a custom sound which can be passed to {@link net.onelitefeather.vulpes.VulpesContent}.
     *
     * @param key   the key of the sound
     * @param range the fixed range of the sound or null for the default attenuation
     * @return the created sound
     * @throws NullPointerException     if the key is null
     * @throws IllegalArgumentException if the range is not finite or not greater than zero
     */
    static CustomSound of(Key key, @Nullable Float range) {
        return new CustomSoundImpl(key, range);
    }

    /**
     * Returns the fixed range of the sound.
     *
     * @return the range or null if the sound uses the default attenuation
     */
    @Nullable Float range();

    /**
     * Returns the Minestom sound event of this sound.
     *
     * @return the sound event
     */
    SoundEvent soundEvent();
}
