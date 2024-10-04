package net.theevilreaper.vulpes.api.room.event;

import net.minestom.server.event.Event;
import net.theevilreaper.vulpes.api.room.Room;
import net.theevilreaper.vulpes.api.room.RoomStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The event would be used to indicates a status update from the {@link Room}.
 * A status update could be when the room reaches his end condition.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public record RoomStatusEvent(@NotNull Room room) implements Event {

    /**
     * Returns the current status from the room.
     * @return the given status
     */
    public @NotNull RoomStatus status() {
        return room.getStatus();
    }
}