package net.theevilreaper.vulpes.api.room.condition;

import net.theevilreaper.vulpes.api.room.Room;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of this interface contains the logic at which points a room reaches his goal.
 * A goal defines this point when a room is finished by the players.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface RoomFinishCondition {

    /**
     * Contains the logic to check if a room reaches his finish condition.
     * @param room the room to check
     * @return true when it reached the goal otherwise false
     */
    boolean finish(@NotNull Room room);
}
