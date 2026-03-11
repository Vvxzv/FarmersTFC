package net.vvxzv.farmerstfc.compat.kubejs.builder;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.registry.FBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockJS;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class DecayingBlockBuilder extends BlockBuilder {
    private static final Consumer<LootBuilder> EMPTY = (loot) -> {
    };
    private ResourceLocation rottenBlock = null;
    private boolean dropSelf = true;
    private int eat = 0;
    private boolean hasFacing = false;

    public DecayingBlockBuilder(ResourceLocation i) {
        super(i);
        this.lootTable = EMPTY;
    }

    public DecayingBlockBuilder setRottenBlock(ResourceLocation rottenBlock) {
        this.rottenBlock = rottenBlock;
        return this;
    }

    public DecayingBlockBuilder eat(int eat) {
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
    public @NotNull Block createObject() {
        ExtendedProperties properties = ExtendedProperties.of(MapColor.COLOR_ORANGE)
                .mapColor(MapColor.COLOR_GREEN)
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .blockEntity(FBlockEntity.DECAYING)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .pushReaction(PushReaction.DESTROY);

        Supplier<Block> selfBlockSupplier = () -> BuiltInRegistries.BLOCK.get(id);
        Supplier<? extends Block> rottedBlockSupplier;

        if (rottenBlock != null) {
            rottedBlockSupplier = () -> BuiltInRegistries.BLOCK.get(rottenBlock);
            return DecayingBlockJS.create(properties.serverTicks(DecayingFoodBlockEntity::serverTick), rottedBlockSupplier, dropSelf, customShape, eat, hasFacing);
        }

        return DecayingBlockJS.create(properties, selfBlockSupplier, dropSelf, customShape, eat, hasFacing);
    }
}