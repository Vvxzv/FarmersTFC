package net.vvxzv.farmerstfc.common.block.decay;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DecayingBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.vvxzv.farmerstfc.common.blockEntity.FDecayingBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
public class FDecayingBlock extends DecayingBlock {
    public FDecayingBlock(ExtendedProperties properties, Supplier<? extends Block> rotted) {
        super(properties, rotted);
    }

    public static boolean isRotten(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FDecayingBlockEntity decaying) {
            return decaying.isRotten();
        }
        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FDecayingBlockEntity decaying) {
            decaying.setStack(stack);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FDecayingBlockEntity decaying) {
            if (player.isCreative()) {
                decaying.setStack(ItemStack.EMPTY);
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof FDecayingBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                Helpers.spawnItem(level, pos, decaying.getStack());
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}