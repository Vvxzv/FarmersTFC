package net.vvxzv.farmerstfc.mixin.block;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.data.DecayToRot;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import net.vvxzv.farmerstfc.common.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

@Mixin(PieBlock.class)
public abstract class PieBlockMixin extends Block implements EntityBlock {
    @Final
    @Shadow(remap = false)
    public static IntegerProperty BITES;

    public PieBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow(remap = false)
    public abstract ItemStack getPieSliceItem();

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DecayingFoodBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return this.getRottedBlock() != null && blockEntityType == BlockEntities.DECAYING.get() ? (l, p, st, be) -> this.serverTick(l, p, st, (DecayingFoodBlockEntity)be) : null;
    }

    @Unique
    private void serverTick(Level level, BlockPos pos, BlockState state, DecayingFoodBlockEntity decaying) {
        if (level.getGameTime() % 20L == 0L && decaying.isRotten()) {
            decaying.setStack(ItemStack.EMPTY);
            level.setBlockAndUpdate(pos, this.getRottedBlock().get().defaultBlockState());
        }
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        IFood food = FoodCapability.get(context.getItemInHand());
        if(food != null && food.isRotten()) {
            Supplier<Block> block = this.getRottedBlock();
            if(block != null) {
                cir.setReturnValue(block.get().defaultBlockState());
            }
        }
    }

    @Unique
    private @Nullable Supplier<Block> getRottedBlock() {
        return DecayToRot.getRotten(this);
    }

    @Redirect(
            method = "cutSlice",
            at = @At(
                    value = "INVOKE",
                    target = "Lvectorwing/farmersdelight/common/utility/ItemUtils;spawnItemEntity(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;DDDDDD)V"
            ),
            remap = false
    )
    private void removeItemSpawn(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
    }

    @Inject(
            method = "cutSlice",
            at = @At("HEAD"),
            remap = false
    )
    private void servingCopyFood(Level level, BlockPos pos, BlockState state, Player player, Item knife, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack newStack = Utils.copyFood(level, pos, this.getPieSliceItem());
        Helpers.spawnItem(level, pos, newStack);
    }

    @Inject(method = "consumeBite", at = @At("HEAD"), cancellable = true, remap = false)
    private void takeRottenServing(Level level, BlockPos pos, BlockState state, Player playerIn, CallbackInfoReturnable<InteractionResult> cir) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Redirect(
            method = "consumeBite",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodData;eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private void eatSlice(FoodData instance, Item pItem, ItemStack pStack, Level level, BlockPos pos, BlockState state, Player playerIn){
        playerIn.eat(level, this.getPieSliceItem());
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DecayingFoodBlockEntity decaying) {
            decaying.setStack(stack);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DecayingFoodBlockEntity decaying) {
            if (player.isCreative()) {
                decaying.setStack(ItemStack.EMPTY);
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                if(state.getValue(BITES) == 0){
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
