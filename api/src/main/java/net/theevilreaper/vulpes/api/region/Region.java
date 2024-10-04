package net.theevilreaper.vulpes.api.region;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.meta.Metadata;
import net.theevilreaper.vulpes.api.util.RegionIntersection;
import net.theevilreaper.vulpes.api.util.VecConditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * The interface defines the structure how a region looks like as class.
 * A region is an area between a local vector V1 and V2. It also contains a {@link Metadata}
 * reference which holds information about what happen in this region.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public interface Region extends RegionIntersection {

    /**
     * Creates a new instance from the {@link RegionBuilder}.
     * @return the created instance
     */
    @Contract(pure = true)
    static @NotNull Builder builder() {
        return new RegionBuilder();
    }

    /**
     * Creates a new instance from the {@link RegionBuilder} from a given reference from a {@link Region}.
     * @return the created builder
     */
    static @NotNull Builder builder(@NotNull Region region) {
        return new RegionBuilder(region);
    }

    /**
     * Returns the lower point of a room as an implementation of {@link Point}.
     *
     * @return the lower corner
     */
    @NotNull Point getLowerCorner();

    /**
     * Returns the height point of a room as an implementation of {@link Point}.
     *
     * @return the height corner
     */
    @NotNull Point getHeightCorner();

    /**
     * Returns the middle point of a region.
     * @return the given middle point
     */
    @Nullable Point getMiddlePoint();

    /**
     * Get a metadata entry by its corresponding key.
     * @param key the key to get the data
     * @return null when they key doesn't have a value otherwise the value
     * @param <V> the generic reference
     */
    @Nullable <V> V getData(@NotNull String key);

    /**
     * Get the object reference from the given {@link Metadata} implementation.
     * @return the reference to it
     */
    @UnknownNullability Metadata getMetaData();

    /**
     * This interface contains all methods to build a {@link Region} instance.
     * The builder implementation is used to create new objects from the {@link Region} class
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.0.0
     */
    sealed interface Builder extends VecConditions permits RegionBuilder {

        /**
         * Set the corner positions to the builder.
         * @param lowerPoint the lower corner to set
         * @param higherPoint the highest corner to set
         * @return the builder instance
         */
        @NotNull Builder corners(@NotNull Vec lowerPoint, @NotNull Vec higherPoint);

        /**
         * Set the metadata implementation which should be used in the region.
         * @param data the meta data implementation to set
         * @return the builder instance
         * @param <T> the type must be inherited from {@link Metadata}
         */
        @NotNull <T extends Metadata> Builder metaData(@NotNull T data);

        /**
         * Returns a new instance from the {@link Region} implementation.
         * @return the created object instance
         */
        @NotNull Region build();
    }
}
