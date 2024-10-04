package net.theevilreaper.vulpes.api.util;

import net.theevilreaper.vulpes.api.room.condition.RoomFinishCondition;

/**
 * The class contains some predefined {@link RoomFinishCondition} conditions.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
public final class RoomConditions {
    public static final long DEFAULT_TICKS = 100L;
    public static final RoomFinishCondition NOP_CONDITION = room -> true;

    private RoomConditions() {
        // Nothing to do in this constructor
    }
}
