package net.theevilreaper.vulpes.api.meta;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
class FightMetaDataTest {

    @Test
    void testMetaDataUpdate() {
        var healthData = new FightMetaData(true);
        assertEquals(1, (int) healthData.getMetaData(FightMetaData.AMOUNT));

        healthData.addData(FightMetaData.AMOUNT, 10);
        assertEquals(10, (int) healthData.getMetaData(FightMetaData.AMOUNT));
    }

    @Test
    void testMetaDataWriteAndRead() {
        var defaultMetaData = new FightMetaData(true);

        var nbt = defaultMetaData.toNBT();

        assertNotNull(nbt);

        var loadedMetaData = new FightMetaData();

        loadedMetaData.readNBT((NBTCompound) nbt);

        assertTrue(defaultMetaData.getMetaData(FightMetaData.MOB_TYPE).toString().isEmpty());
    }

    @Test
    void testGetIdentifier() {
        assertEquals("FightMeta", new FightMetaData().getIdentifier());
    }
}
