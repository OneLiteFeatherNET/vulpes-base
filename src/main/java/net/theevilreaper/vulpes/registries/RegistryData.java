package net.theevilreaper.vulpes.registries;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The {@link RegistryData} class contains helper methods for loading registry data.
 *
 * @author theEvilReaper
 * @version 0.1.0
 * @since 0.6.0
 */
@ApiStatus.Internal
final class RegistryData {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryData.class);

    /**
     * Returns the registry data from the given {@link RegistryResources}.
     *
     * @param registryResources the registry resources to load the data from
     * @return the loaded data or null if the data could not be loaded
     */
    static @Nullable InputStream getRegistryData(@NotNull RegistryResources registryResources) {
        String path = registryResources.getPath();
        InputStream resourceStream = RegistryData.class.getClassLoader().getResourceAsStream(path);
        final Path filesystemPath = Path.of(path);
        if (resourceStream == null && Files.exists(filesystemPath)) {
            try {
                resourceStream = Files.newInputStream(filesystemPath);
            } catch (IOException e) {
                LOGGER.error("Failed to load registry data from filesystem path: {}", filesystemPath, e);
            }
        }
        return resourceStream;
    }

    private RegistryData() {
        // Private constructor to prevent instantiation
    }
}
