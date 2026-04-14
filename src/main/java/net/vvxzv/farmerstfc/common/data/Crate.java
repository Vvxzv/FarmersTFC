package net.vvxzv.farmerstfc.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.vvxzv.farmerstfc.FarmersTFC;
import org.jetbrains.annotations.Nullable;

public record Crate(Ingredient ingredient, ResourceLocation block) {
    public static final Codec<Crate> CODEC = RecordCodecBuilder.create(i -> i.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(c -> c.ingredient),
            ResourceLocation.CODEC.fieldOf("block").forGetter(c -> c.block)
    ).apply(i, Crate::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Crate> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, c -> c.ingredient,
            ResourceLocation.STREAM_CODEC, c -> c.block,
            Crate::new
    );

    public static final DataManager<Crate> MANAGER = new DataManager<>(
            ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "crate"),
            CODEC,
            STREAM_CODEC
    );

    public static final IndirectHashCollection<Item, Crate> CACHE = IndirectHashCollection.create(
            c -> RecipeHelpers.itemKeys(c.ingredient),
            MANAGER::getValues
    );

    public static @Nullable Block getBlock(ItemStack stack) {
        for (Crate crate: CACHE.getAll(stack.getItem())) {
            if(crate.ingredient.test(stack)){
                return BuiltInRegistries.BLOCK.get(crate.block);
            }
        }
        return null;
    }

}
