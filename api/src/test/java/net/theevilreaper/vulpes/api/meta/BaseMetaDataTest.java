package net.theevilreaper.vulpes.api.meta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseMetaDataTest {

    @Test
    void testHasMetaData() {
        assertTrue(new BossMetaData(true).hasMetaData());

        var tickingMeta = new WaitingMeta();
        tickingMeta.removeData(WaitingMeta.TICKING);
        tickingMeta.removeData(WaitingMeta.DELAY);
        tickingMeta.removeData(WaitingMeta.AMOUNT);
        assertFalse(tickingMeta.hasMetaData());
    }

    @Test
    void testCreation() {
        var baseMeta = new BossMetaData();
        assertNotNull(baseMeta);
    }
}
