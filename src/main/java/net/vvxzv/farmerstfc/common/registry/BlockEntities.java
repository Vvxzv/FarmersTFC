package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.block.CrateBlock;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.block.entity.RichSoilFarmlandBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.block.DecayingBlockJS;
import net.vvxzv.farmerstfc.compat.kubejs.block.entity.DecayingBlockEntityJS;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FarmersTFC.MODID);

    private static Block[] getDecayingFoodBlocks() {
        Stream<Block> blocks = Stream.of(
                ModBlocks.ROAST_CHICKEN_BLOCK,
                ModBlocks.STUFFED_PUMPKIN_BLOCK,
                ModBlocks.HONEY_GLAZED_HAM_BLOCK,
                ModBlocks.SHEPHERDS_PIE_BLOCK,
                ModBlocks.RICE_ROLL_MEDLEY_BLOCK,
                ModBlocks.APPLE_PIE,
                ModBlocks.SWEET_BERRY_CHEESECAKE,
                ModBlocks.CHOCOLATE_PIE
        ).map(Supplier::get);

        Stream<Block> crateBlocks = BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof CrateBlock);

        return Stream.concat(blocks, crateBlocks).toArray(Block[]::new);
    }

    public static final Supplier<BlockEntityType<DecayingFoodBlockEntity>> DECAYING = BLOCK_ENTITIES.register(
            "decaying",
            () -> BlockEntityType.Builder.of(
                    DecayingFoodBlockEntity::new,
                    getDecayingFoodBlocks()
            ).build(null)
    );

    public static final Supplier<BlockEntityType<DecayingBlockEntityJS>> KUBEJS_DECAYING = BLOCK_ENTITIES.register(
            "kubejs_decaying",
            () -> BlockEntityType.Builder.of(
                    DecayingBlockEntityJS::new,
                    BuiltInRegistries.BLOCK.stream()
                            .filter(block -> block instanceof DecayingBlockJS)
                            .toArray(Block[]::new)
            ).build(null)
    );

    public static final Supplier<BlockEntityType<RichSoilFarmlandBlockEntity>> FARMLAND = BLOCK_ENTITIES.register(
            "farmland",
            () -> BlockEntityType.Builder.of(
                    RichSoilFarmlandBlockEntity::new,
                    Blocks.RICH_SOIL_FARMLAND.get()
            ).build(null)
    );
}
