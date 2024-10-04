package net.theevilreaper.vulpes.api.meta;

import org.jetbrains.annotations.NotNull;

public final class FightMetaData extends BaseMetaData {

    public static final String MOB_TYPE = "mobType";

    public FightMetaData() {
        super(false);
    }

    public FightMetaData(boolean defaultValues) {
        super(defaultValues);
    }

    @Override
    void initDefaults() {
        addData(META, getIdentifier());
        addData(TICKING, 0);
        addData(MOB_TYPE, "");
        addData(AMOUNT, 1);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "FightMeta";
    }
}
