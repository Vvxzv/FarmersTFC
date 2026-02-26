package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.blockEntity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockBuilder;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class FBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FarmersTFC.MOD_ID);

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

    public static final RegistryObject<BlockEntityType<DecayingFoodBlockEntity>> DECAYING = BLOCK_ENTITIES.register(
            "decaying",
            () -> BlockEntityType.Builder.of(
                    DecayingFoodBlockEntity::new,
                    getFDecayingBlocks()
            ).build(null)
    );
}
