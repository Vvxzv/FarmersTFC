package net.vvxzv.farmerstfc.world.block.decay;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class DecayingHoneyGlazedHamBlock extends DecayingFeastBlock {
    protected static final VoxelShape PLATE_SHAPE = Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)2.0F, (double)15.0F);
    protected static final VoxelShape ROAST_SHAPE;

    public DecayingHoneyGlazedHamBlock(ExtendedProperties properties, Supplier<Item> servingItem, boolean hasLeftovers, Supplier<? extends Block> rotted) {
        super(properties, servingItem, hasLeftovers, rotted);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (Integer)state.getValue(SERVINGS) == 0 ? PLATE_SHAPE : ROAST_SHAPE;
    }

    static {
        ROAST_SHAPE = Shapes.joinUnoptimized(PLATE_SHAPE, Block.box((double)4.0F, (double)2.0F, (double)4.0F, (double)12.0F, (double)10.0F, (double)12.0F), BooleanOp.OR);
    }
}
