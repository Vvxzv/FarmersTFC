package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class itemTagKey {
    public static final TagKey<Item> CANT_COOK;
    public static final TagKey<Item> FOOD_BLOCK;

    static {
        CANT_COOK = TagKey.create(Registries.ITEM, new ResourceLocation("farmerstfc", "cant_cook"));
        FOOD_BLOCK = TagKey.create(Registries.ITEM, new ResourceLocation("farmerstfc", "food_block"));
    }
}
