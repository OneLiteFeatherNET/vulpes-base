package net.theevilreaper.vulpes.api.meta;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class HealthMetaDataTest {

    @Test
    void testMetaDataUpdate() {
        var healthData = new HealthMetaData(true);
        assertEquals(100, (int) healthData.getMetaData(HealthMetaData.TICKS));

        healthData.addData(HealthMetaData.TICKS, 500);
        assertEquals(500, (int) healthData.getMetaData(HealthMetaData.TICKS));
    }

    @Test
    void testMetaDataWriteAndRead() {
        var defaultMetaData = new HealthMetaData(true);

        var nbt = defaultMetaData.toNBT();

        assertNotNull(nbt);

        var loadedMetaData = new HealthMetaData();

        loadedMetaData.readNBT((NBTCompound) nbt);

        assertEquals((int)defaultMetaData.getMetaData(HealthMetaData.TICKS), (int) loadedMetaData.getMetaData(HealthMetaData.TICKS));
        assertNull(defaultMetaData.getMetaData(Metadata.TICKING));
    }

    @Test
    void testGetIdentifier() {
        assertNotEquals("TestData", new HealthMetaData().getIdentifier());
    }
}
