package net.vvxzv.farmerstfc.compat.kubejs.builder;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.vvxzv.farmerstfc.common.block.CrateBlock;

import java.util.function.Consumer;

public class CrateBlockBuilder extends BlockBuilder {
    private static final Consumer<LootBuilder> EMPTY = (loot) -> {};

    public CrateBlockBuilder(ResourceLocation id) {
        super(id);
        this.lootTable = EMPTY;
    }

    @Override
    public Block createObject() {
        return new CrateBlock();
    }
}
