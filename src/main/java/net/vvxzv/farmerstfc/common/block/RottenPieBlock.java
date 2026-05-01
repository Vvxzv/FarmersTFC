package net.vvxzv.farmerstfc.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RottenPieBlock extends Block {
    public RottenPieBlock() {
        super(Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        level.destroyBlock(pos, true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(8.0F, 0.0F, 2.0F, 14.0F, 4.0F, 8.0F);
    }
}
