package net.theevilreaper.vulpes.api.util;

import net.minestom.server.coordinate.Point;
import org.jetbrains.annotations.NotNull;

/**
 * The interface defines some methods (at the moment only one) which executes different checks on positions.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public interface VecConditions {

    /**
     * Checks if the given higher corner is lower than the lower point.
     * @param lowerPoint the lower point for the check
     * @param higherPoint the higher point for the check
     * @return true when the higher point is smaller than the lower point otherwise false
     */
    default boolean validateCorners(@NotNull Point lowerPoint, @NotNull Point higherPoint) {
        return higherPoint.x() < lowerPoint.x() && higherPoint.y() < lowerPoint.y() && higherPoint.z() < lowerPoint.z();
    }
}
