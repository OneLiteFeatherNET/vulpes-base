package net.theevilreaper.vulpes.api.util;

import net.minestom.server.coordinate.Point;
import org.jetbrains.annotations.NotNull;

/**
 * The interface contains methods to check if a point intersects with the area of a region.
 * It has different method to check if the intersection is only on two or three axis.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RegionIntersection {

    /**
     * Checks if a given point intersect in a region with the x and z axis.
     * @param point the point to check
     * @return true when the point intersects otherwise false
     */
    boolean intersectXZ(@NotNull Point point);

    /**
     * Checks if a point intersects on all three dimension with the region
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    boolean intersectXYZ(@NotNull Point point);
}
