package net.theevilreaper.vulpes.item;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ItemMetaData {

    String key();

    ItemRarity rarity() default ItemRarity.NORMAL;

    ItemType type() default ItemType.PASSIV;

    ItemCategory category() default ItemCategory.HEAL;

}
