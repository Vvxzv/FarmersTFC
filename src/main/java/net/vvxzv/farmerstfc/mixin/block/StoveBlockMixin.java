package net.vvxzv.farmerstfc.mixin.block;

import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.block.entity.StoveBlockEntity;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.CommonTags;

@Mixin(StoveBlock.class)
public abstract class StoveBlockMixin extends AbstractStoveBlock {

    public StoveBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "newBlockEntity", at = @At("RETURN"), cancellable = true)
    public void newBlockEntity(BlockPos pos, BlockState state, CallbackInfoReturnable<BlockEntity> cir) {
        cir.setReturnValue(new StoveBlockEntity(pos, state));
    }

    @Inject(method = "getTicker", at = @At("RETURN"), cancellable = true)
    public <T extends BlockEntity> void getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        cir.setReturnValue(blockEntityType == BlockEntities.STOVE.get() ? (l, pos, s, be) -> ((StoveBlockEntity) be).serverTick(l, pos, s) : null);
    }

    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        CompoundTag tag = context.getItemInHand().getOrCreateTag();
        if(tag.contains("extinguish")) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(LIT, false);
        }
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LIT, true);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        CompoundTag tag = stack.getOrCreateTag();
        if(Config.stoveNeedsFuel && tag.contains("extinguish")) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof StoveBlockEntity stove) {
            stove.initFuel();
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof StoveBlockEntity stove) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                for (ItemStack fuel : stove.getFuels()) {
                    Helpers.spawnItem(level, pos, fuel);
                }

                ItemStack stoveItem = new ItemStack(ModBlocks.STOVE.get());
                if(Config.stoveNeedsFuel) {
                    CompoundTag tag = stoveItem.getOrCreateTag();
                    tag.putBoolean("extinguish", true);
                    stoveItem.setTag(tag);
                }
                Helpers.spawnItem(level, pos, stoveItem);
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    }
}
