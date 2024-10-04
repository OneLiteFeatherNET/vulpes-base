package net.theevilreaper.vulpes.api.room;

/**
 * The enum contains all states which a room currently have.
 * So the flow is when a room gets loaded it has the status {@link #LOADED}.
 * Over the life span of a room reference the states goes to {@link #RUNNING} and at least to {@link #END}
 * when all regions are computed
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public enum RoomStatus {

    LOADED, RUNNING, END;
}
