package net.theevilreaper.vulpes.api.meta;

import org.jetbrains.annotations.NotNull;

public final class BossMetaData extends BaseMetaData {

    public static final String TYPE = "type";
    public static final String CATEGORY = "category";

    public BossMetaData() {
        super(false);
    }

    public BossMetaData(boolean defaultValues) {
        super(defaultValues);
    }

    @Override
    void initDefaults() {
        addData(META, getIdentifier());
        addData(TICKING, true);
        addData(TYPE, "test");
        addData(CATEGORY, "ts");
    }

    @Override
    public @NotNull String getIdentifier() {
        return "BossMeta";
    }
}
