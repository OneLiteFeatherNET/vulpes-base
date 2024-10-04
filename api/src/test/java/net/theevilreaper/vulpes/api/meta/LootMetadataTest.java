package net.theevilreaper.vulpes.api.meta;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LootMetadataTest {

    @Test
    void testLootMetaDataUpdate() {
        var lootMeta = new LootMetadata(true);
        assertEquals(1, (int) lootMeta.getMetaData("amount"));

        int amount = (int) lootMeta.getMetaData("amount") + 10;
        lootMeta.addData("amount", amount);
        assertEquals(11, (int) lootMeta.getMetaData("amount"));
    }

    @Test
    void testMetaDataWriteAndRead() {
        var defaultMetaData = new LootMetadata(true);

        var nbt = defaultMetaData.toNBT();

        assertNotNull(nbt);

        var loadedMetaData = new LootMetadata();

        loadedMetaData.readNBT((NBTCompound) nbt);

        assertEquals(defaultMetaData.getMetaData(LootMetadata.LOOT), (String) loadedMetaData.getMetaData(LootMetadata.LOOT));
        assertEquals((int) defaultMetaData.getMetaData(LootMetadata.AMOUNT), (int) loadedMetaData.getMetaData("amount"));
        assertEquals(defaultMetaData.getMetaData("rarity"), (String) loadedMetaData.getMetaData("rarity"));
        assertEquals(defaultMetaData.getIdentifier(), loadedMetaData.getIdentifier());
    }
}
