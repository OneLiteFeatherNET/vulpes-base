package net.theevilreaper.vulpes.api.region;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.meta.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

/**
 * The class is the implementation of the {@link Region} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class RegionImpl implements Region {

    private final Vec lowerPoint;
    private final Vec higherPoint;
    private final Metadata metadata;
    private final Vec middlePoint;

    /**
     * Creates a new object reference from the {@link RegionImpl} class.
     * @param lowerPoint the lower point to set
     * @param higherPoint the higher point to set
     * @param metadata the included metadata for the room
     */
    RegionImpl(@NotNull Vec lowerPoint, @NotNull Vec higherPoint, @NotNull Metadata metadata) {
        this.lowerPoint = lowerPoint;
        this.higherPoint = higherPoint;
        this.metadata = metadata;
        if (this.lowerPoint.samePoint(this.higherPoint)) {
            this.middlePoint = this.lowerPoint;
        } else {
            // Determines the middle point only when lower and highest point are not equal
            this.middlePoint = higherPoint.add(lowerPoint).mul(.5);
        }
    }

    /**
     * Returns an indication if the given {@link Point} intersects with the region.
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    @Override
    public boolean intersectXZ(@NotNull Point point) {
        return point.x() >= lowerPoint.x() && point.z() >= lowerPoint.z()
                && point.x() <= higherPoint.x() && point.z() <= higherPoint.z();
    }

    /**
     * Returns an indication if the given {@link Point} intersects with the region.
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    @Override
    public boolean intersectXYZ(@NotNull Point point) {
        return intersectXZ(point) && point.y() >= lowerPoint.y() && point.y() <= higherPoint.y();
    }

    /**
     * Makes a check if this object is equal to another reference.
     * @param o the object to check
     * @return true when they are equals otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionImpl region = (RegionImpl) o;
        return Objects.equals(lowerPoint, region.lowerPoint) && Objects.equals(higherPoint, region.higherPoint);
    }

    /**
     * Generates a hash value for the {@link Region} object.
     * @return the created hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(lowerPoint, higherPoint);
    }

    /**
     * Get the point which represents the lower corner of the region.
     * @return the given lower point
     */
    @Override
    public @NotNull Point getLowerCorner() {
        return this.lowerPoint;
    }

    /**
     * Get the point which represents the highest corner of the region.
     * @return the given highest point
     */
    @Override
    public @NotNull Point getHeightCorner() {
        return this.higherPoint;
    }

    /**
     * Return the middle point from the region.
     * @return the determined middle point
     */
    @Override
    public @Nullable Point getMiddlePoint() {
        return this.middlePoint;
    }

    /**
     * Get a metadata entry by its corresponding key.
     * @param key the key to get the data
     * @return null when they key doesn't have a value otherwise the value
     * @param <V> the generic reference
     */
    @Override
    public <V> @Nullable V getData(@NotNull String key) {
        return this.metadata.getMetaData(key);
    }

    /**
     * Get the object reference from the given {@link Metadata} implementation.
     * @return the reference to it
     */
    @UnknownNullability("The metadata from a region can't be null but the getter from a room can return null")
    @Override
    public Metadata getMetaData() {
        return this.metadata;
    }
}
