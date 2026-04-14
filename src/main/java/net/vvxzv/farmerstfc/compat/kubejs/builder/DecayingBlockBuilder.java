package net.vvxzv.farmerstfc.compat.kubejs.builder;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import net.vvxzv.farmerstfc.compat.kubejs.block.DecayingBlockJS;
import net.vvxzv.farmerstfc.compat.kubejs.block.entity.DecayingBlockEntityJS;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class DecayingBlockBuilder extends BlockBuilder {
    private ResourceLocation rottenBlock;
    private Boolean dropSelf = true;
    private int eat = 0;
    private boolean hasFacing = false;

    public DecayingBlockBuilder(ResourceLocation i) {
        super(i);
    }

    public DecayingBlockBuilder setRottenBlock(ResourceLocation rottenBlock){
        this.rottenBlock = rottenBlock;
        return this;
    }

    public DecayingBlockBuilder eat(int eat){
        this.eat = eat;
        return this;
    }

    public DecayingBlockBuilder facing() {
        this.hasFacing = true;
        return this;
    }

    @Override
    public BlockBuilder noDrops() {
        this.dropSelf = false;
        return this;
    }

    @Override
    public @Nullable LootTable generateLootTable(KubeDataGenerator generator) {
        return null;
    }

    @Override
    public @NotNull Block createObject(){
        ExtendedProperties properties = ExtendedProperties.of(MapColor.COLOR_ORANGE)
                .mapColor(MapColor.COLOR_GREEN)
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .blockEntity(BlockEntities.KUBEJS_DECAYING)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .pushReaction(PushReaction.DESTROY);

        Supplier<Block> selfBlockSupplier = () -> BuiltInRegistries.BLOCK.get(id);
        Supplier<? extends Block> rottedBlockSupplier;

        if (rottenBlock != null) {
            rottedBlockSupplier = () -> BuiltInRegistries.BLOCK.get(rottenBlock);
            return DecayingBlockJS.create(properties.serverTicks(DecayingBlockEntityJS::serverTick), rottedBlockSupplier, dropSelf, customShape, eat, hasFacing);
        }

        return DecayingBlockJS.create(properties, selfBlockSupplier, dropSelf, customShape, eat, hasFacing);
    }
}