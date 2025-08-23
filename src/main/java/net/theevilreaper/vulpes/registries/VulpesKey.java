package net.theevilreaper.vulpes.registries;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link VulpesKey} interface contains the basic method structure for different kind of keys.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public interface VulpesKey<T> extends Keyed {

    /**
     * Returns the name of the key.
     *
     * @return the given name
     */
    default @NotNull String name() {
        return this.key().asString();
    }
}
