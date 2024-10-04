package net.theevilreaper.vulpes.api.room;

import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.meta.WaitingMeta;
import net.theevilreaper.vulpes.api.region.Region;
import net.theevilreaper.vulpes.api.util.PosTranslators;
import net.theevilreaper.vulpes.api.util.RoomConditions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomTest {

    @Test
    void testRoomCreation() {
        Vec lower = new Vec(0,0,0);
        Vec higher = new Vec(100, 100, 100);
        Room dungeonRoom = Room.builder()
                .corners(lower, higher)
                .translator(PosTranslators::translate)
                .finishCondition(RoomConditions.NOP_CONDITION)
                .build();
        assertNotNull(dungeonRoom);
        assertEquals(lower, dungeonRoom.getLowerCorner());
        assertEquals(higher, dungeonRoom.getHeightCorner());
        assertTrue(dungeonRoom.getFinishCondition().finish(dungeonRoom));
        assertEquals(RoomStatus.LOADED, dungeonRoom.getStatus());
    }

    @Test
    void testRoomUpdate() {
        var room = Room.builder()
                .corners(Vec.ZERO, Vec.ONE)
                .build();
        assertNotNull(room);

        var updatedRoom = Room.builder(room)
                .corners(Vec.ZERO, new Vec(24))
                .build();

        assertNotEquals(room, updatedRoom);
    }

    @Test
    void testRoomCreationWithAllValues() {
        var regions = new ArrayDeque<Region>();
        regions.add(Region.builder().corners(Vec.ZERO, new Vec(5)).metaData(new WaitingMeta()).build());
        var room = Room.builder()
                .corners(Vec.ZERO, new Vec(10))
                .finishCondition(RoomConditions.NOP_CONDITION)
                .translator(PosTranslators::translate)
                .tick(RoomTest::tick)
                .regions(regions)
                .finishCondition(room1 -> room1.getRegions().isEmpty())
                .build();
        assertEquals(1, room.getRegions().size());
        assertNull(room.getMiddlePoint());
        assertEquals(RoomStatus.LOADED, room.getStatus());
        var translatedPoint = room.getTranslator().translateVector(Vec.fromPoint(room.getLowerCorner()), new Vec(3), PositionTranslator.Mode.TO_RELATIVE);
        assertEquals(new Vec(3), translatedPoint);
        assertNull(room.getActiveRegion());
        assertFalse(room.isLastRegion());
        assertNull(room.getData("key"));
    }

    @Test
    void testRoomIntersection() {
        var room = Room.builder()
                .corners(Vec.ZERO, new Vec(10))
                .tick(RoomTest::tick)
                .build();
        assertTrue(room.intersectRoom(new Vec(5)));
    }

    @Test
    void testRoomCreationWithSwappedPositions() {
        Vec lower = new Vec(25,25,15);
        Vec higher = new Vec(10);
        Room room = Room.builder().corners(higher, lower).build();
        assertNotNull(room);
        assertEquals(higher, room.getLowerCorner());
        assertEquals(lower, room.getHeightCorner());
    }

    private static void tick(long ms, @NotNull Region region) {}
}
