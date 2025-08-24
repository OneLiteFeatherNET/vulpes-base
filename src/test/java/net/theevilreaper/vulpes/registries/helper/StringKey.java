package net.theevilreaper.vulpes.registries.helper;

import net.kyori.adventure.key.Key;
import net.theevilreaper.vulpes.registries.VulpesKey;
import org.jetbrains.annotations.NotNull;

/**
 * Simple registry object for testing.
 *
 * @param key the key for the object
 */
public record StringKey(@NotNull Key key) implements VulpesKey<StringKey> {
}
