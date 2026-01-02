package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FItemTag {
    public static final TagKey<Item> CANT_COOK;

    static {
        CANT_COOK = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("farmerstfc", "cant_cook"));
    }
}
