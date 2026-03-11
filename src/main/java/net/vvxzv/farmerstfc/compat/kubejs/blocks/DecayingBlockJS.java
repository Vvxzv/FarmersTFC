package net.vvxzv.farmerstfc.compat.kubejs.blocks;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vvxzv.farmerstfc.common.block.decay.DecayingFoodBlock;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.utils.BlockPropertyUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class DecayingBlockJS extends DecayingFoodBlock {
    public DirectionProperty facing;
    public IntegerProperty eat;
    public boolean isDropSelf;
    public final int maxEat;

    private final boolean hasFacing;
    private final VoxelShape combinedShape;

    private DecayingBlockJS(ExtendedProperties properties, Supplier<? extends Block> rotted, boolean dropSelf, List<AABB> customShapes, int pEat, boolean facing) {
        super(properties, rotted);

        this.hasFacing = facing;
        this.isDropSelf = dropSelf;
        this.maxEat = pEat;

        if (customShapes.isEmpty()) {
            combinedShape = Shapes.block();
        } else {
            VoxelShape shape = Shapes.empty();
            for (AABB aabb : customShapes) {
                shape = Shapes.or(shape, Shapes.create(aabb));
            }
            combinedShape = shape.optimize();
        }
    }

    public static DecayingBlockJS create(ExtendedProperties properties, Supplier<? extends Block> rotted, boolean dropSelf, List<AABB> customShapes, int pEat, boolean facing) {
        DecayingBlockJS block = new DecayingBlockJS(properties, rotted, dropSelf, customShapes, pEat, facing);
        block.initStateDefinition();
        return block;
    }

    private void initStateDefinition() {
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);

        int maxEat = Math.max(this.maxEat, 1);
        this.eat = IntegerProperty.create("eat", 0, maxEat);
        builder.add(this.eat);

        if (this.hasFacing) {
            this.facing = BlockStateProperties.HORIZONTAL_FACING;
            builder.add(this.facing);
        }
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);

        BlockState defaultState = this.stateDefinition.any().setValue(this.eat, this.maxEat);

        if (this.hasFacing) {
            defaultState = defaultState.setValue(this.facing, Direction.NORTH);
        }

        this.registerDefaultState(defaultState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            state = this.defaultBlockState();
        }

        if (this.eat != null) {
            state = state.setValue(this.eat, this.maxEat);
        }

        if (this.hasFacing && this.facing != null) {
            Direction dir = context.getHorizontalDirection().getOpposite();
            state = state.setValue(this.facing, dir);
        }

        return state;
    }

    public static int getMaxEat(BlockState state){
        if (state.getBlock() instanceof DecayingBlockJS blockJS){
            return blockJS.maxEat;
        }
        return 0;
    }

    public static int getEatValue(BlockState state) {
        if (state.getBlock() instanceof DecayingBlockJS blockJS){
            return state.getValue(blockJS.eat);
        }
        return 0;
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Info("arg0: BlockState state, arg1: String facing \"north\" | \"south\" | \"west\" | \"east\"")
    public static BlockState setFacing(BlockState state, String facing) {
        Direction d = BlockPropertyUtils.DIRECTION_MAP.get(facing);
        if (d == null) return state;
        return setFacing(state, d);
    }

    @Info("BlockState state, Direction facing")
    public static BlockState setFacing(BlockState state, Direction facing) {
        if (state.getBlock() instanceof DecayingBlockJS blockJS && blockJS.facing != null) {
            return state.setValue(blockJS.facing, facing);
        }
        return state;
    }

    @Info("BlockState state, String number")
    public static BlockState setEat(BlockState state, String number) {
        try {
            int n = Integer.parseInt(number);
            if (state.getBlock() instanceof DecayingBlockJS blockJS && blockJS.eat != null) {
                int valueToSet = Math.min(n, blockJS.maxEat);
                return state.setValue(blockJS.eat, valueToSet);
            }
        } catch (NumberFormatException ignored) {
        }
        return state;
    }

    @Info("Level level, BlockPos pos, boolean drop")
    public static void setDrop(Level level, BlockPos pos, boolean drop) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            decaying.setDropSelf(drop);
        }
    }

    @Info("Level level, BlockPos pos, String addEat, boolean drop")
    public static BlockState consume(Level level, BlockPos pos, String addEat, boolean drop) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof DecayingBlockJS blockJS && blockJS.eat != null) {
            try {
                int currentEat = state.getValue(blockJS.eat);
                int addAmount = Integer.parseInt(addEat);
                int newEat = currentEat + addAmount;

                setDrop(level, pos, drop);

                int valueToSet = Math.min(newEat, blockJS.maxEat);
                return state.setValue(blockJS.eat, valueToSet);
            } catch (NumberFormatException ignored) {
            }
        }
        return state;
    }

    @Info("Level level, BlockPos pos, String addEat")
    public static BlockState consume(Level level, BlockPos pos, String addEat) {
        return consume(level, pos, addEat, false);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return combinedShape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return combinedShape;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DecayingFoodBlockEntity decaying) {
            decaying.setDropSelf(this.isDropSelf);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock()) && decaying.isDropSelf()) {
                Helpers.spawnItem(level, pos, decaying.getStack());
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    }
}