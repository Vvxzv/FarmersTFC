package net.vvxzv.farmerstfc.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;

public class PanBlock extends Block {
    public static final DirectionProperty FACING;
    public static final BooleanProperty SUPPORT;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape SHAPE;
    protected static final VoxelShape SHAPE_WITH_TRAY;

    public PanBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(SUPPORT, false)).setValue(WATERLOGGED, false));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ((Boolean)state.getValue(SUPPORT)).equals(true) ? SHAPE_WITH_TRAY : SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        FluidState fluid = level.getFluidState(context.getClickedPos());
        return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)).setValue(SUPPORT, this.getTrayState(context.getLevel(), context.getClickedPos()));
    }

    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean)state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return facing.getAxis().equals(Direction.Axis.Y) ? (BlockState)state.setValue(SUPPORT, this.getTrayState(level, currentPos)) : state;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, SUPPORT, WATERLOGGED});
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private boolean getTrayState(LevelAccessor world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES);
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        SUPPORT = BooleanProperty.create("support");
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPE = Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)4.0F, (double)15.0F);
        SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box((double)0.0F, (double)-1.0F, (double)0.0F, (double)16.0F, (double)0.0F, (double)16.0F));
    }
}
