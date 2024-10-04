package net.theevilreaper.vulpes.api.meta;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WaitingMetaTest {

    @Test
    void testMetaDataUpdate() {
        var healthData = new WaitingMeta(true);
        assertEquals(100, (int) healthData.getMetaData(WaitingMeta.DELAY));

        healthData.addData(WaitingMeta.DELAY, 500);
        assertEquals(500, (int) healthData.getMetaData(WaitingMeta.DELAY));
    }

    @Test
    void testMetaDataWriteAndRead() {
        var defaultMetaData = new WaitingMeta(true);

        var nbt = defaultMetaData.toNBT();

        assertNotNull(nbt);

        var loadedMetaData = new WaitingMeta();

        loadedMetaData.readNBT((NBTCompound) nbt);

        assertEquals((int)defaultMetaData.getMetaData(WaitingMeta.DELAY), (int) loadedMetaData.getMetaData(WaitingMeta.DELAY));
        assertTrue((boolean) loadedMetaData.getMetaData(Metadata.TICKING));
    }

    @Test
    void testGetIdentifier() {
        assertEquals("WaitingMeta", new WaitingMeta().getIdentifier());
    }
}
