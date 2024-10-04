package net.theevilreaper.vulpes.api.meta;

import org.jetbrains.annotations.NotNull;

public final class HealthMetaData extends BaseMetaData {

    public static final String TICKS = "ticks";
    private static final int DEFAULT_TICKS = 100;
    private static final int DEFAULT_HEALTH = 50;

    public HealthMetaData() {
        super(false);
    }

    public HealthMetaData(boolean defaultValues) {
        super(defaultValues);
    }

    @Override
    void initDefaults() {
        addData(META, getIdentifier());
        addData(TICKS, true);
        addData(TICKS, DEFAULT_TICKS);
        addData(AMOUNT, DEFAULT_HEALTH);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "HealthMeta";
    }
}
