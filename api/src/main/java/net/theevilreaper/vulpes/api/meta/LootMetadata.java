package net.theevilreaper.vulpes.api.meta;

import net.theevilreaper.vulpes.api.ItemCategory;
import net.theevilreaper.vulpes.api.ItemRarity;
import org.jetbrains.annotations.NotNull;

public final class LootMetadata extends BaseMetaData {
    
    public static final String RARITY = "rarity";
    public static final String LOOT = "lootType";

    public LootMetadata() {
        super(false);
    }

    public LootMetadata(boolean defaultValues) {
        super(defaultValues);
    }

    @Override
    void initDefaults() {
        addData(TICKING, true);
        addData(LOOT, ItemCategory.HEAL.name().toLowerCase());
        addData(AMOUNT, 1);
        addData(RARITY, ItemRarity.NORMAL.name().toLowerCase());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "LootMeta";
    }
}
