package net.theevilreaper.vulpes.api.room.event;

import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.room.Room;
import net.theevilreaper.vulpes.api.room.RoomStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RoomStatusEventTest {

    @Test
    void testEventConstruction() {
        var room = Room.builder().corners(Vec.ZERO, Vec.ONE).build();
        RoomStatusEvent event = new RoomStatusEvent(room);
        assertNotEquals(RoomStatus.RUNNING, event.status());
    }

}