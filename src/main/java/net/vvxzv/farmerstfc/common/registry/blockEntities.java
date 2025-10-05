package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.blockEntity.FDecayingBlockEntity;
import net.vvxzv.farmerstfc.common.blockEntity.RichSoilFarmlandBlockEntity;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class blockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final Supplier<BlockEntityType<FDecayingBlockEntity>> DECAYING;
    public static final Supplier<BlockEntityType<RichSoilFarmlandBlockEntity>> FARMLAND;

    private static Block[] getDecayingBlocks() {
        return Stream.of(
                block.ROAST_CHICKEN_BLOCK,
                block.STUFFED_PUMPKIN_BLOCK,
                block.HONEY_GLAZED_HAM_BLOCK,
                block.SHEPHERDS_PIE_BLOCK,
                block.RICE_ROLL_MEDLEY_BLOCK,
                block.APPLE_PIE,
                block.SWEET_BERRY_CHEESECAKE,
                block.CHOCOLATE_PIE
        ).map(Supplier::get).toArray(Block[]::new);
    }

    static {
        BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FarmersTFC.MODID);

        DECAYING = BLOCK_ENTITIES.register(
                "decaying",
                () -> BlockEntityType.Builder.of(
                        FDecayingBlockEntity::new,
                        getDecayingBlocks()
                ).build(null)
        );

        FARMLAND = BLOCK_ENTITIES.register(
                "farmland",
                () -> BlockEntityType.Builder.of(
                        RichSoilFarmlandBlockEntity::new,
                        block.RICH_SOIL_FARMLAND.get()
                ).build(null)
        );
    }
}
