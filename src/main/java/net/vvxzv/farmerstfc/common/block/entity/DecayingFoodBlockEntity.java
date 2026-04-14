package net.vvxzv.farmerstfc.common.block.entity;

import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class DecayingFoodBlockEntity extends TFCBlockEntity {
    private ItemStack stack;

    public DecayingFoodBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntities.DECAYING.get(), pos, state);
    }

    protected DecayingFoodBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.stack = ItemStack.EMPTY;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack.copyWithCount(1);
    }

    public void setStackWithCount(ItemStack stack) {
        this.stack = stack;
    }

    public @NotNull ItemStack getStack() {
        return this.stack;
    }

    public @NotNull ItemStack copyStack() {
        return this.stack.copy();
    }

    public boolean isRotten() {
        return this.stack.isEmpty() || FoodCapability.isRotten(this.stack);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        this.stack = ItemStack.parseOptional(provider, nbt.getCompound("item"));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        if (!this.stack.isEmpty()) {
            nbt.put("item", this.stack.save(provider));
        }
    }
}
