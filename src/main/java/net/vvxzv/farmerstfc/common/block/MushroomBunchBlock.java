package net.vvxzv.farmerstfc.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MushroomBunchBlock extends BushBlock {
    public static final MapCodec<MushroomBunchBlock> CODEC = simpleCodec(MushroomBunchBlock::new);

    public MushroomBunchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockPos floorPos = pos.below();
        BlockState floorState = level.getBlockState(floorPos);
        return floorState.isSolidRender(level, floorPos) && floorState.canSustainPlant(level, floorPos, Direction.UP, state).isDefault();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.isSolidRender(level, pos);
    }
}