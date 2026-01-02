package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.blockEntity.FDecayingBlockEntity;
import net.vvxzv.farmerstfc.common.blockEntity.RichSoilFarmlandBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockBuilder;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class FBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final Supplier<BlockEntityType<FDecayingBlockEntity>> DECAYING;
    public static final Supplier<BlockEntityType<RichSoilFarmlandBlockEntity>> FARMLAND;

    private static Block[] getFDecayingBlocks() {
        // 获取本模组代码注册的特定方块
        Stream<Block> modBlocks = Stream.of(
                FBlock.ROAST_CHICKEN_BLOCK,
                FBlock.STUFFED_PUMPKIN_BLOCK,
                FBlock.HONEY_GLAZED_HAM_BLOCK,
                FBlock.SHEPHERDS_PIE_BLOCK,
                FBlock.RICE_ROLL_MEDLEY_BLOCK,
                FBlock.APPLE_PIE,
                FBlock.SWEET_BERRY_CHEESECAKE,
                FBlock.CHOCOLATE_PIE
        ).map(Supplier::get);

        // 获取KubeJS注册的方块
        Stream<Block> kubejsBlocks = BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof DecayingBlockBuilder.DecayingBlockJS);

        // 合并两个流并转换为数组
        return Stream.concat(modBlocks, kubejsBlocks).toArray(Block[]::new);
    }


    static {
        BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FarmersTFC.MODID);

        DECAYING = BLOCK_ENTITIES.register(
                "decaying",
                () -> BlockEntityType.Builder.of(
                        FDecayingBlockEntity::new,
                        getFDecayingBlocks()
                ).build(null)
        );

        FARMLAND = BLOCK_ENTITIES.register(
                "farmland",
                () -> BlockEntityType.Builder.of(
                        RichSoilFarmlandBlockEntity::new,
                        FBlock.RICH_SOIL_FARMLAND.get()
                ).build(null)
        );
    }
}
