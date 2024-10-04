package net.theevilreaper.vulpes.api.room;

import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of the {@link PositionTranslator} interface contains the logic to translate vectors from
 * an absolute vector which represents a position from an {@link net.minestom.server.instance.InstanceContainer} to a relative and reversed.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
@FunctionalInterface
public interface PositionTranslator {

    /**
     * Translates a given vector to a relative or absolute vector.
     * @param mainPoint the main point
     * @param translatePoint the point to translate
     * @param mode the mode to determine which way of translation should be used
     * @return the translated vector
     */
    @NotNull Vec translateVector(@NotNull Vec mainPoint, @NotNull Vec translatePoint, @NotNull Mode mode);

    /**
     * Contains all available modes for the translation process
     * @version 1.0.0
     * @since 1.0.0
     * @author theEvilReaper
     */
    enum Mode {

        TO_RELATIVE, TO_ABSOLUTE
    }
}
