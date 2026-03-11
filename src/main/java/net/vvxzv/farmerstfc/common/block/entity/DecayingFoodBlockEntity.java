package net.vvxzv.farmerstfc.common.block.entity;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.common.block.decay.DecayingFoodBlock;
import net.vvxzv.farmerstfc.common.registry.FBlockEntity;

public class DecayingFoodBlockEntity extends DecayingBlockEntity{
    private boolean dropSelf = true;

    public DecayingFoodBlockEntity(BlockPos pos, BlockState state) {
        super(FBlockEntity.DECAYING.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if(blockEntity instanceof DecayingFoodBlockEntity decaying){
            if (level.getGameTime() % 20L == 0L && decaying.isRotten()) {
                if (blockState.getBlock() instanceof DecayingFoodBlock block) {
                    decaying.setStack(ItemStack.EMPTY);
                    level.setBlockAndUpdate(blockPos, block.getRottedBlock().defaultBlockState());
                }
            }
        }
    }

    public void setDropSelf(boolean dropSelf){
        this.dropSelf = dropSelf;
    }

    public boolean isDropSelf() {
        return this.dropSelf;
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.dropSelf = nbt.getBoolean("dropSelf");
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putBoolean("dropSelf", this.dropSelf);
    }
}
