package net.theevilreaper.vulpes.api.room;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.meta.Metadata;
import net.theevilreaper.vulpes.api.region.Region;
import net.theevilreaper.vulpes.api.region.RegionTick;
import net.theevilreaper.vulpes.api.room.condition.RoomFinishCondition;
import net.theevilreaper.vulpes.api.room.event.RoomStatusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The class is the implementation of the {@link Room} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class RoomImpl implements Room {

    private final PositionTranslator positionTranslator;
    private final ArrayDeque<Region> rooms;
    private final RegionTick regionTick;
    private final RoomFinishCondition roomFinishCondition;
    private final Vec lowerPoint;
    private final Vec higherPoint;
    private RoomStatus status;
    private Region activeRegion;
    private boolean isTickable;

    /**
     * Creates a new reference from the class with the given values.
     * @param lowerPoint the minimum point
     * @param higherPoint the maximum point
     * @param regions all regions which are included into the room
     * @param regionTick the handling for the tick logic
     * @param positionTranslator the translation implementation for the positions
     * @param roomFinishCondition and the condition when a room is done
     */
    RoomImpl(
            @NotNull Vec lowerPoint,
            @NotNull Vec higherPoint,
            @NotNull ArrayDeque<Region> regions,
            @NotNull RegionTick regionTick,
            @NotNull PositionTranslator positionTranslator,
            @NotNull RoomFinishCondition roomFinishCondition) {
        this.lowerPoint = lowerPoint;
        this.higherPoint = higherPoint;
        this.rooms = regions;
        this.regionTick = regionTick;
        this.positionTranslator = positionTranslator;
        this.roomFinishCondition = roomFinishCondition;
        this.status = RoomStatus.LOADED;
    }

    /**
     * Gets the next region which for the execution order of a room. When there is no region left nothing will
     * happen in this method
     */
    @Override
    public void peekRegion() {
        // Break method execution when the status is RoomStatus.END
        if (this.status == RoomStatus.END) return;
        if (this.status == RoomStatus.LOADED) {
            this.status = RoomStatus.RUNNING;
            MinecraftServer.getGlobalEventHandler().call(new RoomStatusEvent(this));
        }
        if (!getRegions().isEmpty()) {
            this.activeRegion = getRegions().pollLast();
            this.isTickable = this.activeRegion.getData(Metadata.TICKING) != null;
            return;
        }
        this.status = RoomStatus.END;
        MinecraftServer.getGlobalEventHandler().call(new RoomStatusEvent(this));
        this.clear();
    }

    /**
     * Returns an indication if the given {@link Point} intersects with the room.
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    @Override
    public boolean intersectRoom(@NotNull Point point) {
        return point.x() >= lowerPoint.x() && point.y() >= lowerPoint.y()
                && point.z() >= lowerPoint.z()
                && point.x() <= higherPoint.x() && point.y() <= higherPoint.y()
                && point.z() <= higherPoint.z();
    }

    /**
     * Returns an indication if the given {@link Point} intersects with the active region.
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    @Override
    public boolean intersectXZ(@NotNull Point point) {
        return this.activeRegion != null && this.activeRegion.intersectXZ(point);
    }

    /**
     * Returns an indication if the given {@link Point} intersects with active region.
     * @param point the point to check
     * @return true when it intersects otherwise false
     */
    @Override
    public boolean intersectXYZ(@NotNull Point point) {
        return this.activeRegion != null && this.activeRegion.intersectXYZ(point);
    }

    /**
     * Clears some meta value which are used for the room logic.
     */
    private void clear() {
        this.activeRegion = null;
        this.isTickable = false;
    }

    /**
     * Contains the logic to tick a region if necessary.
     * @param time the time of the tick in milliseconds
     */
    @Override
    public void tick(long time) {
        if (this.activeRegion != null && this.isTickable) {
            this.regionTick.onTick(time, activeRegion);
        }
    }

    /**
     * Returns the implementation of {@link PositionTranslator] which handles the translation of the {@link Point} values.
     * @return the given reference the translator
     */
    @Override
    public @NotNull PositionTranslator getTranslator() {
        return this.positionTranslator;
    }

    /**
     * Returns the middle point from the current active region.
     * @return null when no region is active otherwise the middle point
     */
    @Override
    public @Nullable Point getMiddlePoint() {
        return this.activeRegion == null ? null : this.activeRegion.getMiddlePoint();
    }

    /**
     * Returns the condition wish indicates that the room is done.
     * @return the given condition implementation
     */
    @Override
    public @NotNull RoomFinishCondition getFinishCondition() {
        return this.roomFinishCondition;
    }

    /**
     * Returns the current active region from the room.
     * @return the active region or null when no region is active
     */
    @Override
    public @Nullable Region getActiveRegion() {
        return this.activeRegion;
    }

    /**
     * Get the lower corner from the room.
     * @return the given position
     */
    @Override
    public @NotNull Point getLowerCorner() {
        return this.lowerPoint;
    }

    /**
     * Get the height corner from the room.
     * @return the given position
     */
    @Override
    public @NotNull Point getHeightCorner() {
        return this.higherPoint;
    }

    /**
     * Get a specific metadata entry by its corresponding key.
     * @param key the key to get the data
     * @return null when the key doesn't have a value otherwise the value
     * @param <V> the return value
     */
    @Override
    public <V> @Nullable V getData(@NotNull String key) {
        return this.activeRegion == null ? null : activeRegion.getData(key);
    }

    /**
     * Returns the {@link Metadata} object which holds all metadata values for the room.
     * @return the given object
     */
    @UnknownNullability("When the active region is null the getter returns also null")
    @Override
    public Metadata getMetaData() {
        return this.activeRegion == null ? null : this.activeRegion.getMetaData();
    }

    /**
     * Returns an implementation of the {@link Deque} which holds all regions from the room.
     * @return the given {@link Deque} with the rooms
     */
    @Override
    public @NotNull Deque<Region> getRegions() {
        return this.rooms;
    }

    /**
     * Return the given {@link RoomStatus} from the room.
     * @return the current status
     */
    @Override
    public @NotNull RoomStatus getStatus() {
        return status;
    }
}
