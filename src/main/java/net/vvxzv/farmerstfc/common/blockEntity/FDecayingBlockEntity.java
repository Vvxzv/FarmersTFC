package net.vvxzv.farmerstfc.common.blockEntity;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.common.block.decay.FDecayingBlock;
import net.vvxzv.farmerstfc.common.registry.blockEntities;

public class FDecayingBlockEntity extends DecayingBlockEntity{
    private ItemStack stack;

    public FDecayingBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType) blockEntities.DECAYING.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if(blockEntity instanceof FDecayingBlockEntity decaying){
            if (level.getGameTime() % 20L == 0L && decaying.isRotten()) {
                if (blockState.getBlock() instanceof FDecayingBlock block) {
                    decaying.setStack(ItemStack.EMPTY);
                    level.setBlockAndUpdate(blockPos, block.getRottedBlock().defaultBlockState());
                }
            }
        }
    }
}
