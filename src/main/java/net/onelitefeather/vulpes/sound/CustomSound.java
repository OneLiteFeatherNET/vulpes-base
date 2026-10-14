package net.onelitefeather.vulpes.sound;

import net.kyori.adventure.key.Key;
import net.minestom.server.sound.SoundEvent;
import net.onelitefeather.vulpes.registries.Registry;
import net.onelitefeather.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A custom sound which is defined in code and available through a runtime registry.
 * <p>
 * The constants are inherited from the generated {@code CustomSounds} interface, the keys
 * are available through {@link CustomSoundKeys}.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public sealed interface CustomSound extends VulpesKey, CustomSounds permits CustomSoundImpl {

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

    /**
     * Returns the sound for the given key.
     *
     * @param key the key to look up
     * @return the sound or null if no sound is registered for the key
     */
    static @Nullable CustomSound fromKey(Key key) {
        return CustomSoundImpl.REGISTRY.get(key);
    }

    /**
     * Returns all registered sounds.
     *
     * @return the registered sounds
     */
    static Collection<CustomSound> values() {
        return CustomSoundImpl.REGISTRY.values();
    }

    /**
     * Returns the registry which contains all custom sounds.
     *
     * @return the registry
     */
    static Registry<CustomSound> registry() {
        return CustomSoundImpl.REGISTRY;
    }
}
