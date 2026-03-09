package net.onelitefeather.vulpes.registries;

import net.kyori.adventure.key.Keyed;

/**
 * The {@link VulpesKey} interface contains the basic method structure for different kind of keys.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 */
public interface VulpesKey extends Keyed {

    /**
     * Returns the name of the key.
     *
     * @return the given name
     */
    default String name() {
        return this.key().asString();
    }
}
