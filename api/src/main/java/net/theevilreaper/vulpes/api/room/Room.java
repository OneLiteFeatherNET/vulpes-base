package net.theevilreaper.vulpes.api.room;

import net.minestom.server.Tickable;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.region.Region;
import net.theevilreaper.vulpes.api.region.RegionTick;
import net.theevilreaper.vulpes.api.room.condition.RoomFinishCondition;
import net.theevilreaper.vulpes.api.util.VecConditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The room interface defines a basic structure which each room implementation must provide.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
public interface Room extends Region, Tickable {

    /**
     * Creates a new reference from the {@link RoomBuilder} which allows the creation of a {@link RoomImpl} class.
     * @return the builder instance
     */
    @Contract(pure = true)
    static @NotNull Builder builder() {
        return new RoomBuilder();
    }

    /**
     * Creates a new builder reference to build a room with a given instance from a room.
     * @param room the room for the builder
     * @return the created instance
     */
    @Contract(pure = true)
    static @NotNull Builder builder(@NotNull Room room) {
        return new RoomBuilder(room);
    }

    /**
     * The implementation of the method pulls the next room from the given data structure.
     * It does this only when the structure contains room elements otherwise the execution of the method will be ignored.
     */
    void peekRegion();

    /**
     * Checks if a given point intersects with the area from the room.
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    boolean intersectRoom(@NotNull Point point);

    /**
     * Returns an indicator if the region stack is empty or not.
     * @return true when the stack is empty otherwise false
     */
    default boolean isLastRegion() {
        return getRegions().isEmpty();
    }

    /**
     * Returns the {@link PositionTranslator} instance which allows the translation of {@link Point} values.
     * @return the given instance from an {@link PositionTranslator} implementation
     */
    @NotNull PositionTranslator getTranslator();

    /**
     * Returns the middle point of a room as an implementation of {@link Point}.
     *
     * @return the middle point or null
     */
    @Nullable Point getMiddlePoint();

    /**
     * Returns the implementation for the {@link RoomFinishCondition} of a room.
     * @return the given finishCondition
     */
    @NotNull RoomFinishCondition getFinishCondition();

    /**
     * Returns the current active region.
     * It can be nullable when the room stack is empty or the room hasn't started yet.
     * @return the given active region
     */
    @Nullable Region getActiveRegion();

    /**
     * The {@link Deque} implementation which holds all regions.
     * @return the given {@link Deque}
     */
    @NotNull Deque<Region> getRegions();

    /**
     * Returns the current {@link RoomStatus} from a room.
     * @return the given status
     */
    @NotNull RoomStatus getStatus();

    /**
     * The {@link Builder} interface defines the structure to build a region.
     * Also, the builder is the only way to create new objects from the {@link Room}.
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.0.0
     */
    sealed interface Builder extends VecConditions permits RoomBuilder {

        /**
         * Sets the boundary {@link Vec}'s for the room.
         * @param lowerPoint the lower point
         * @param higherPoint the higher point
         * @return the builder instance
         */
        @NotNull Builder corners(@NotNull Vec lowerPoint, @NotNull Vec higherPoint);

        /**
         * Set an implementation of the {@link PositionTranslator} which translates the positions.
         * @param translator the translator to set
         * @return the builder instance
         */
        @NotNull Builder translator(@NotNull PositionTranslator translator);

        /**
         * Set the implementation of {@link RegionTick} interface which holds the logic to tick rooms.
         * @param regionTick the regionTick to set
         * @return the builder instance
         */
        @NotNull Builder tick(@NotNull RegionTick regionTick);

        /**
         * Set all regions which are available for the room.
         * @param regions to set
         * @return the builder instance
         */
        @NotNull Builder regions(@NotNull ArrayDeque<Region> regions);

        /**
         * Set an implementation for the {@link RoomFinishCondition} which is used to check if a room reach his goal.
         * @param finishCondition the condition to set
         * @return the builder instance
         */
        @NotNull Builder finishCondition(@NotNull RoomFinishCondition finishCondition);

        /**
         * Creates a new object reference from the {@link Room} class.
         * @return the created instance
         */
        @NotNull Room build();
    }
}
