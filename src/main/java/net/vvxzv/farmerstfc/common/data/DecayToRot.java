package net.vvxzv.farmerstfc.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.vvxzv.farmerstfc.FarmersTFC;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.Supplier;

public record DecayToRot(ResourceLocation decayingBlock, ResourceLocation rottenBlock) {
    public static final Codec<DecayToRot> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.fieldOf("decayingBlock").forGetter(c -> c.decayingBlock),
            ResourceLocation.CODEC.fieldOf("rottenBlock").forGetter(c -> c.rottenBlock)
    ).apply(i, DecayToRot::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DecayToRot> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, c -> c.decayingBlock,
            ResourceLocation.STREAM_CODEC, c -> c.rottenBlock,
            DecayToRot::new
    );

    public static final DataManager<DecayToRot> MANAGER = new DataManager<>(
            ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "decay_to_rot"),
            CODEC,
            STREAM_CODEC
    );

    public static final IndirectHashCollection<ResourceLocation, DecayToRot> CACHE = IndirectHashCollection.create(
            c -> Collections.singleton(c.decayingBlock),
            MANAGER::getValues
    );

    public static @Nullable Supplier<Block> getRotten(ResourceLocation decay) {
        for (DecayToRot decayToRot: CACHE.getAll(decay)) {
            if(decayToRot.decayingBlock.equals(decay)){
                return () -> BuiltInRegistries.BLOCK.get(decayToRot.rottenBlock);
            }
        }
        return null;
    }

    public static @Nullable Supplier<Block> getRotten(Block decay) {
        return getRotten(BuiltInRegistries.BLOCK.getKey(decay));
    }

    public static @Nullable ResourceLocation getRottenRL(ResourceLocation decay) {
        for (DecayToRot decayToRot: CACHE.getAll(decay)) {
            if(decayToRot.decayingBlock.equals(decay)){
                return decayToRot.rottenBlock;
            }
        }
        return null;
    }
}
