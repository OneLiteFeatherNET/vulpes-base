package net.theevilreaper.vulpes.api.region;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.validate.Check;
import net.theevilreaper.vulpes.api.meta.Metadata;
import org.jetbrains.annotations.NotNull;

/**
 * The class is the implementation of the {@link Region.Builder} interface. Over the class it easy
 * to create new region objects with the given values
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public non-sealed class RegionBuilder implements Region.Builder {

    private Vec lowerPoint;
    private Vec higherPoint;
    private Metadata metadata;

    /**
     * Empty constructor
     */
    public RegionBuilder() {}

    /**
     * Creates a new builder reference which uses the data from a {@link Region} reference.
     * @param region the region to get the data from it
     */
    public RegionBuilder(@NotNull Region region) {
        this.lowerPoint = Vec.fromPoint(region.getLowerCorner());
        this.higherPoint = Vec.fromPoint(region.getHeightCorner());
        this.metadata = region.getMetaData();
    }

    /**
     * Set the corner values to the region builder.
     * @param lowerPoint the lower corner to set
     * @param higherPoint the highest corner to set
     * @return this
     */
    @Override
    public @NotNull Region.Builder corners(@NotNull Vec lowerPoint, @NotNull Vec higherPoint) {
        this.lowerPoint = lowerPoint;
        this.higherPoint = higherPoint;
        return this;
    }

    /**
     * Set the {@link Metadata} implementation for the region.
     * @param data the meta data implementation to set
     * @return this
     * @param <T> the class must extends from the {@link Metadata} interface
     */
    @Override
    public <T extends Metadata> Region.@NotNull Builder metaData(@NotNull T data) {
        this.metadata = data;
        return this;
    }

    /**
     * Creates the new region with the given values.
     * @return the crated region
     */
    @Override
    public @NotNull Region build() {
        Check.argCondition(this.metadata == null, "The region must have a metadata");
        // Check if the lower and higher point are swapped to swap them back
        if (validateCorners(this.lowerPoint, this.higherPoint)) {
            var temp = this.higherPoint;
            this.higherPoint = this.lowerPoint;
            this.lowerPoint = temp;
        }
        return new RegionImpl(this.lowerPoint, this.higherPoint, this.metadata);
    }
}
