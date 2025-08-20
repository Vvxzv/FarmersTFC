package net.vvxzv.farmerstfc.world.registry;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.farmerstfc.farmersTFC;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class blockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final RegistryObject<BlockEntityType<DecayingBlockEntity>> DECAYING;

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks) {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks);
    }

    static {
        BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, farmersTFC.MOD_ID);
        DECAYING = register("decaying", DecayingBlockEntity::new, Stream.of(block.ROAST_CHICKEN_BLOCK, block.STUFFED_PUMPKIN_BLOCK, block.HONEY_GLAZED_HAM_BLOCK, block.SHEPHERDS_PIE_BLOCK, block.RICE_ROLL_MEDLEY_BLOCK, block.APPLE_PIE, block.SWEET_BERRY_CHEESECAKE, block.CHOCOLATE_PIE));
    }
}
