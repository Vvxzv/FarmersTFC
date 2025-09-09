package net.vvxzv.farmerstfc.compat.kubejs.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DecayingBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class DecayingBlockBuilder extends BlockBuilder {
    private ResourceLocation rottenBlock;

    public DecayingBlockBuilder(ResourceLocation i) {
        super(i);
    }

    public DecayingBlockBuilder setRottenBlock(ResourceLocation rottenBlock){
        this.rottenBlock = rottenBlock;
        return this;
    }

    @Override
    public @NotNull Block createObject(){
        Supplier<? extends Block> rottedBlockSupplier = getSupplier();

        ExtendedProperties properties = ExtendedProperties.of(MapColor.COLOR_ORANGE)
                .mapColor(MapColor.COLOR_GREEN)
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .blockEntity(TFCBlockEntities.DECAYING)
                .serverTicks(DecayingBlockEntity::serverTick)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .pushReaction(PushReaction.DESTROY);

        return new DecayingBlock(properties, rottedBlockSupplier);
    }

    private @NotNull Supplier<? extends Block> getSupplier() {
        Supplier<Block> selfBlockSupplier = () -> BuiltInRegistries.BLOCK.get(id);
        Supplier<? extends Block> rottedBlockSupplier;
        if (rottenBlock != null) {
            rottedBlockSupplier = () -> {
                Block rottedBlock = BuiltInRegistries.BLOCK.get(rottenBlock);
                if (rottedBlock == Blocks.AIR) {
                    return selfBlockSupplier.get();
                }
                return rottedBlock;
            };
        } else {
            rottedBlockSupplier = selfBlockSupplier;
        }
        return rottedBlockSupplier;
    }
}