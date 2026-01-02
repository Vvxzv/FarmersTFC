package net.vvxzv.farmerstfc.common.block.decay;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class DecayingRoastChickenBlock extends DecayingFeastBlock{
    protected static final VoxelShape PLATE_SHAPE = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 2.0F, 15.0F);
    protected static final VoxelShape ROAST_SHAPE = Shapes.joinUnoptimized(PLATE_SHAPE, Block.box(4.0F, 2.0F, 4.0F, 12.0F, 9.0F, 12.0F), BooleanOp.OR);

    public DecayingRoastChickenBlock(ExtendedProperties properties, Supplier<Item> servingItem, boolean hasLeftovers, Supplier<? extends Block> rotted) {
        super(properties, servingItem, hasLeftovers, rotted);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(SERVINGS) == 0 ? PLATE_SHAPE : ROAST_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        IFood food = FoodCapability.get(context.getItemInHand());
        return food != null && food.isRotten() ? this.getRottedBlock().defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()) : this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
