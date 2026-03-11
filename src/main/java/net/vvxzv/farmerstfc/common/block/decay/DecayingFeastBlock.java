package net.vvxzv.farmerstfc.common.block.decay;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Supplier;

public class DecayingFeastBlock extends DecayingFoodBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);
    protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.box(2.0F, 0.0F, 2.0F, 14.0F, 1.0F, 14.0F), Block.box(2.0F, 0.0F, 2.0F, 14.0F, 3.0F, 14.0F), Block.box(2.0F, 0.0F, 2.0F, 14.0F, 6.0F, 14.0F), Block.box(2.0F, 0.0F, 2.0F, 14.0F, 8.0F, 14.0F), Block.box(2.0F, 0.0F, 2.0F, 14.0F, 10.0F, 14.0F)};

    public final Supplier<Item> servingItem;
    public final boolean hasLeftovers;

    public DecayingFeastBlock(ExtendedProperties properties, Supplier<Item> servingItem, boolean hasLeftovers, Supplier<? extends Block> rotted) {
        super(properties, rotted);
        this.servingItem = servingItem;
        this.hasLeftovers = hasLeftovers;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(this.getServingsProperty(), this.getMaxServings()));
    }

    public IntegerProperty getServingsProperty() {
        return SERVINGS;
    }

    public int getMaxServings() {
        return 4;
    }

    public ItemStack getServingItem(BlockState state) {
        return new ItemStack(this.servingItem.get());
    }

    private ItemStack copyCreationDate(LevelAccessor level, BlockPos pos, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            IFood decayingIFood = FoodCapability.get(decaying.getStack());
            FoodCapability.setCreationDate(stack, decayingIFood.getCreationDate());
        }
        return stack;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(SERVINGS)];
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        return level.isClientSide && this.takeServing(level, pos, state, player, hand).consumesAction() ? ItemInteractionResult.SUCCESS : this.takeServing(level, pos, state, player, hand);
    }

    protected ItemInteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        int servings = state.getValue(this.getServingsProperty());
        if (servings == 0) {
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            level.destroyBlock(pos, true);
            return ItemInteractionResult.SUCCESS;
        } else {
            ItemStack serving = this.copyCreationDate(level, pos, this.getServingItem(state));
            ItemStack heldStack = player.getItemInHand(hand);
            if (servings > 0) {
                if (!serving.hasCraftingRemainingItem() || ItemStack.isSameItem(heldStack, serving.getCraftingRemainingItem())) {
                    level.setBlock(pos, state.setValue(this.getServingsProperty(), servings - 1), 3);
                    if (!player.getAbilities().instabuild && serving.hasCraftingRemainingItem()) {
                        heldStack.shrink(1);
                    }

                    if (!player.getInventory().add(serving)) {
                        player.drop(serving, false);
                    }

                    if (level.getBlockState(pos).getValue(this.getServingsProperty()) == 0 && !this.hasLeftovers) {
                        level.removeBlock(pos, false);
                    }

                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    return ItemInteractionResult.SUCCESS;
                }

                player.displayClientMessage(TextUtils.getTranslation("block.feast.use_container", serving.getCraftingRemainingItem().getHoverName()), true);
            }

            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        IFood food = FoodCapability.get(context.getItemInHand());
        return food != null && food.isRotten() ? this.getRottedBlock().defaultBlockState() : this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SERVINGS);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return blockState.getValue(this.getServingsProperty());
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                if(state.getValue(SERVINGS) == 4){
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }

        if (state.hasBlockEntity() && !state.is(newState.getBlock())) {
            level.removeBlockEntity(pos);
        }
    }
}
