package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.vvxzv.farmerstfc.FarmersTFC;

@SuppressWarnings("removal")
public class FItemTag {
    public static final TagKey<Item> CANT_COOK = TagKey.create(Registries.ITEM, new ResourceLocation(FarmersTFC.MOD_ID, "cant_cook"));
}
