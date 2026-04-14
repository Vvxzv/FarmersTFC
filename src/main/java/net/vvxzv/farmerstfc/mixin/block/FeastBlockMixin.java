package net.vvxzv.farmerstfc.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

@Mixin(FeastBlock.class)
public abstract class FeastBlockMixin extends Block implements EntityBlock {
    @Final
    @Shadow
    public static IntegerProperty SERVINGS;

    @Shadow
    @Final
    public static DirectionProperty FACING;

    @Shadow
    public abstract ItemStack getServingItem(BlockState state);

    public FeastBlockMixin(Properties properties) {
        super(properties);
    }

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

    @Unique
    private @Nullable Supplier<Block> getRottedBlock() {
        return DecayToRot.getRotten(this);
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        IFood food = FoodCapability.get(context.getItemInHand());
        if(food != null && food.isRotten()) {
            Supplier<Block> block = this.getRottedBlock();
            if(block != null) {
                Direction facing = cir.getReturnValue().getValue(FACING);
                BlockState defaultBlockState = block.get().defaultBlockState();
                cir.setReturnValue(
                        defaultBlockState.hasProperty(FACING)
                                ? defaultBlockState.setValue(FACING, facing)
                                : defaultBlockState
                );
            }
        }
    }

    @ModifyVariable(
            method = "takeServing",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            name = "serving"
    )
    private ItemStack servingCopyFood(
            ItemStack originalServing,
            @Local(argsOnly = true) LevelAccessor level,
            @Local(argsOnly = true) BlockPos pos,
            @Local(argsOnly = true) BlockState state
    ) {
        return Utils.copyFood(level, pos, this.getServingItem(state));
    }

    @Inject(method = "takeServing", at = @At("HEAD"), cancellable = true)
    private void takeRottenServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand, CallbackInfoReturnable<ItemInteractionResult> cir) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<ItemInteractionResult> cir) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
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
                if(state.getValue(SERVINGS) == 4){
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
