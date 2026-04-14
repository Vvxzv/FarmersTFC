package net.vvxzv.farmerstfc.compat.kubejs.builder;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.vvxzv.farmerstfc.common.block.CrateBlock;
import org.jetbrains.annotations.Nullable;

public class CrateBlockBuilder extends BlockBuilder {
    public CrateBlockBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    public @Nullable LootTable generateLootTable(KubeDataGenerator generator) {
        return null;
    }

    @Override
    public Block createObject() {
        return new CrateBlock();
    }
}
