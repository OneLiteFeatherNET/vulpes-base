package net.theevilreaper.vulpes.api.region;

import org.jetbrains.annotations.NotNull;

/**
 * The interface contains a method which can be implemented to handle the logic for
 * a region which needs to tick.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@FunctionalInterface
public interface RegionTick {

    /**
     * Holds the logic to tick a region.
     * @param ms the tick length
     * @param activeRegion the involved region
     */
    void onTick(long ms, @NotNull Region activeRegion);
}
