package net.theevilreaper.vulpes.api.meta;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BossMetaDataTest {

    @Test
    void testMetaDataUpdate() {
        var bossMeta = new BossMetaData(true);
        assertEquals("test", bossMeta.getMetaData(BossMetaData.TYPE));

        bossMeta.addData(BossMetaData.TYPE, "Radeos");
        assertEquals("Radeos", bossMeta.getMetaData(BossMetaData.TYPE));
    }

    @Test
    void testMetaDataWriteAndRead() {
        var defaultMetaData = new BossMetaData(true);

        var nbt = defaultMetaData.toNBT();

        assertNotNull(nbt);

        var loadedMetaData = new BossMetaData();

        loadedMetaData.readNBT((NBTCompound) nbt);

        assertFalse(defaultMetaData.getMetaData(BossMetaData.TYPE).toString().isEmpty());
        assertTrue((boolean) loadedMetaData.getMetaData(Metadata.TICKING));
    }

    @Test
    void testGetIdentifier() {
        assertNotEquals("FightMeta", new BossMetaData().getIdentifier());
    }
}
