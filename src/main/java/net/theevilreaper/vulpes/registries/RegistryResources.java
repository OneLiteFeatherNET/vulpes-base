package net.theevilreaper.vulpes.registries;

import org.jetbrains.annotations.NotNull;

/**
 * Enum containing all resources used by the registry.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.6.0
 */
public enum RegistryResources {

    BITMAP_FONTS("assets/fonts/bitmap.json"),
    ;

    private final String path;

    /**
     * Creates a new instance of the resource.
     *
     * @param path the path to the resource
     */
    RegistryResources(final String path) {
        this.path = path;
    }

    /**
     * Returns the path to the resource.
     *
     * @return the path
     */
    public @NotNull String getPath() {
        return path;
    }
}
