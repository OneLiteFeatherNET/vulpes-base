package net.onelitefeather.vulpes.entity.holder;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.onelitefeather.vulpes.item.AbstractItem;

/**
 * Implementation for a static holder entity.
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 **/
public class Holder extends ItemHolderEntity {

    /**
     * Creates a new instance from the holder entity.
     * @param instance The instance for the entity
     * @param spawnPoint The spawn point from the entity
     * @param item The item for the entity to hold
     */
    public Holder(Instance instance, Point spawnPoint, AbstractItem item) {
        super(instance, spawnPoint, item);
    }

    @Override
    public void tick(long time) {
        // Nothing to do
    }
}
