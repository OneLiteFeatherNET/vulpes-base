package net.theevilreaper.vulpes.api.meta;

import org.jetbrains.annotations.NotNull;

/**
 * The {@link WaitingMeta} can be used to make a small delay between regions.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class WaitingMeta extends BaseMetaData {

    public static final String DELAY = "delay";

    public WaitingMeta() {
        super(false);
    }

    public WaitingMeta(boolean defaultValues) {
        super(defaultValues);
    }

    /**
     * Set some default values to the data structure.
     */
    @Override
    void initDefaults() {
        addData(META, getIdentifier());
        addData(TICKING, true);
        addData(DELAY, 100);
    }

    /**
     * Returns the identifier from the meta implementation.
     * @return the given identifier
     */
    @Override
    public @NotNull String getIdentifier() {
        return "WaitingMeta";
    }
}
