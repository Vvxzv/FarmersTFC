package net.vvxzv.farmerstfc.compat.kubejs.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import net.vvxzv.farmerstfc.compat.kubejs.block.DecayingBlockJS;
import org.jetbrains.annotations.NotNull;

public class DecayingBlockEntityJS extends DecayingFoodBlockEntity {
    private boolean dropSelf = true;

    public DecayingBlockEntityJS(BlockPos pos, BlockState state) {
        super(BlockEntities.KUBEJS_DECAYING.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if(blockEntity instanceof DecayingFoodBlockEntity decaying){
            if (level.getGameTime() % 20L == 0L && decaying.isRotten()) {
                if (blockState.getBlock() instanceof DecayingBlockJS block) {
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
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        this.dropSelf = nbt.getBoolean("dropSelf");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putBoolean("dropSelf", this.dropSelf);
    }
}
