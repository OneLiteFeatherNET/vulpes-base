package net.theevilreaper.vulpes.api.region;

import net.minestom.server.coordinate.Vec;
import net.theevilreaper.vulpes.api.meta.WaitingMeta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegionTest {

    @Test
    void testRegionCreation() {
        var region = Region.builder()
                .corners(new Vec(0,0,0), new Vec(10, 10, 10))
                .metaData(new WaitingMeta())
                .build();
        assertNotNull(region);
        assertEquals(Vec.ZERO, region.getLowerCorner());
        assertEquals(new Vec(10, 10, 10), region.getHeightCorner());
        assertEquals(new Vec(5,5,5), region.getMiddlePoint());
        assertNotNull(region.getMetaData());
    }

    @Test
    void testPointRegionCreation() {
        var region = Region.builder()
                .corners(Vec.ONE, Vec.ONE)
                .metaData(new WaitingMeta())
                .build();
        assertEquals(region.getMiddlePoint(), region.getLowerCorner());
        assertNull(region.getData("key"));
        assertTrue(region.getMetaData() instanceof WaitingMeta);
    }

    @Test
    void testRegionIntersects() {
        var region = Region.builder()
                .corners(Vec.ZERO, new Vec(20, 20 ,20))
                .metaData(new WaitingMeta())
                .build();
        assertTrue(region.intersectXZ(Vec.ZERO));
        assertTrue(region.intersectXZ(new Vec(20, -20, 10)));
        assertTrue(region.intersectXYZ(new Vec(10)));
        assertFalse(region.intersectXYZ(new Vec(-20)));
    }

    @Test
    void testRegionEquals() {
        var firstRegion = Region.builder().corners(Vec.ZERO, Vec.ONE).metaData(new WaitingMeta()).build();
        var secondRegion = Region.builder().corners(Vec.ZERO, Vec.ONE).metaData(new WaitingMeta()).build();

        assertEquals(firstRegion, secondRegion);
        assertEquals(firstRegion.hashCode(), secondRegion.hashCode());

        secondRegion = Region.builder(secondRegion).corners(Vec.ZERO, new Vec(10, 20, 20)).build();

        assertNotEquals(firstRegion, secondRegion);
        assertNotEquals(firstRegion.hashCode(), secondRegion.hashCode());
    }
}
