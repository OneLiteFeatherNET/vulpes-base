package net.theevilreaper.vulpes.api.room;

import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.region.Region;
import net.theevilreaper.vulpes.api.region.RegionTick;
import net.theevilreaper.vulpes.api.room.condition.RoomFinishCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

/**
 * The class is the implementation of the {@link Room.Builder} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public non-sealed class RoomBuilder implements Room.Builder {

    private RoomFinishCondition finishCondition;
    private PositionTranslator translator;
    private RegionTick regionTick;
    private ArrayDeque<Region> regions;
    private Vec lowerPoint;
    private Vec higherPoint;

    /**
     * Empty constructor for the builder.
     */
    public RoomBuilder() { }

    /**
     * Creates a new builder from an exiting {@link Room} object
     * @param room the room for the builder to get the data from it
     */
    public RoomBuilder(@NotNull Room room) {
        this.lowerPoint = Vec.fromPoint(room.getLowerCorner());
        this.higherPoint = Vec.fromPoint(room.getHeightCorner());
        this.translator = room.getTranslator();
        if (this.regions != null) {
            this.regions = new ArrayDeque<>(room.getRegions());
        } else {
            this.regions = new ArrayDeque<>();
        }
        this.finishCondition = room.getFinishCondition();
    }

    /**
     * Set the corner positions of the room.
     * @param lowerPoint the lower point
     * @param higherPoint the higher point
     * @return the builder instance
     */
    @Override
    public Room.@NotNull Builder corners(@NotNull Vec lowerPoint, @NotNull Vec higherPoint) {
        this.lowerPoint = lowerPoint;
        this.higherPoint = higherPoint;
        return this;
    }

    /**
     * Set the class which handles the translation for the positions.
     * @param translator the translator to set
     * @return the builder instance
     */
    @Override
    public Room.@NotNull Builder translator(@NotNull PositionTranslator translator) {
        this.translator = translator;
        return this;
    }

    /**
     * Set the implementation of the {@link RegionTick} interface which contains the logic for the room tick.
     * @param regionTick the regionTick to set
     * @return the builder instance
     */
    @Override
    public Room.@NotNull Builder tick(@NotNull RegionTick regionTick) {
        this.regionTick = regionTick;
        return this;
    }

    /**
     * Set the structure which holds an all regions for the room
     * @param regions to set
     * @return the builder instance
     */
    @Override
    public Room.@NotNull Builder regions(@NotNull ArrayDeque<Region> regions) {
        this.regions = regions;
        return this;
    }

    /**
     * Set the condition which indicates if the room is done or not
     * @param finishCondition the condition to set
     * @return the builder instance
     */
    @Override
    public Room.@NotNull Builder finishCondition(@NotNull RoomFinishCondition finishCondition) {
        this.finishCondition = finishCondition;
        return this;
    }

    /**
     * Creates a new object reference from a {@link Room}.
     * @return the created reference
     */
    @Override
    public @NotNull Room build() {
        if (validateCorners(this.lowerPoint, this.higherPoint)) {
            var temp = this.higherPoint;
            this.higherPoint = this.lowerPoint;
            this.lowerPoint = temp;
        }
        return new RoomImpl(lowerPoint, higherPoint, regions, regionTick, translator, finishCondition);
    }
}
