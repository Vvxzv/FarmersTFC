package net.vvxzv.farmerstfc.compat.kubejs.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vvxzv.farmerstfc.common.block.decay.DecayingFoodBlock;
import net.vvxzv.farmerstfc.common.blockEntity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.registry.FBlockEntity;
import net.vvxzv.farmerstfc.common.util.BlockPropertyUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class DecayingBlockBuilder extends BlockBuilder {
    private ResourceLocation rottenBlock;
    private Boolean dropSelf = true;
    private int eat = 0;

    public DecayingBlockBuilder(ResourceLocation i) {
        super(i);
    }

    public DecayingBlockBuilder setRottenBlock(ResourceLocation rottenBlock){
        this.rottenBlock = rottenBlock;
        return this;
    }

    public DecayingBlockBuilder eat(int eat){
        this.eat = eat;
        return this;
    }

    @Override
    public BlockBuilder noDrops() {
        this.dropSelf = false;
        return this;
    }

    @Override
    public @Nullable LootTable generateLootTable() {
        return null;
    }

    private @NotNull Supplier<? extends Block> getRottenBlockSupplier() {
        Supplier<Block> selfBlockSupplier = () -> BuiltInRegistries.BLOCK.get(id);
        Supplier<? extends Block> rottedBlockSupplier;
        if (rottenBlock != null) {
            rottedBlockSupplier = () -> {
                Block rottedBlock = BuiltInRegistries.BLOCK.get(rottenBlock);
                if (rottedBlock == Blocks.AIR) {
                    return selfBlockSupplier.get();
                }
                return rottedBlock;
            };
        } else {
            rottedBlockSupplier = selfBlockSupplier;
        }
        return rottedBlockSupplier;
    }

    @Override
    public @NotNull Block createObject(){
        Supplier<? extends Block> rottedBlockSupplier = getRottenBlockSupplier();

        ExtendedProperties properties = ExtendedProperties.of(MapColor.COLOR_ORANGE)
                .mapColor(MapColor.COLOR_GREEN)
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .blockEntity(FBlockEntity.DECAYING)
                .serverTicks(DecayingFoodBlockEntity::serverTick)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .pushReaction(PushReaction.DESTROY);

        return new DecayingBlockJS(properties, rottedBlockSupplier, dropSelf, customShape, eat);
    }

    public static class DecayingBlockJS extends DecayingFoodBlock {
        public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
        public static final BooleanProperty DROP_SELF = BooleanProperty.create("drop_self");
        public static final IntegerProperty EAT = IntegerProperty.create("eat", 0, 15);
        private final VoxelShape combinedShape;

        public DecayingBlockJS(ExtendedProperties properties, Supplier<? extends Block> rotted, Boolean dropSelf, List<AABB> customShapes, int eat) {
            super(properties, rotted);

            this.registerDefaultState(
                    this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(DROP_SELF, dropSelf)
                        .setValue(EAT, Math.min(eat, 15))
            );

            if (customShapes.isEmpty()) {
                combinedShape = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            } else {
                VoxelShape shape = Shapes.empty();
                for (AABB aabb : customShapes) {
                    shape = Shapes.or(shape, Shapes.create(aabb));
                }
                combinedShape = shape.optimize();
            }
        }

        @Info("arg0: BlockState state, arg1: String facing \"north\" | \"south\" | \"west\" | \"east\"")
        public static BlockState setFacing(BlockState state, String facing){
            Direction d = BlockPropertyUtils.DIRECTION_MAP.get(facing);
            if(d == null) return state;
            return state.setValue(FACING, d);
        }

        @Info("BlockState state, Direction facing")
        public static BlockState setFacing(BlockState state, Direction facing){
            return state.setValue(FACING, facing);
        }

        @Info("arg0: BlockState state, arg1: String number \"0\"~\"15\"")
        public static BlockState setEat(BlockState state, String number){
            Integer n = BlockPropertyUtils.INTEGER_MAP.get(number);
            if(n == null) return state;
            return state.setValue(EAT, n);
        }

        @Info("arg0: BlockState state, arg1: String drop \"true\" | \"false\"")
        public static BlockState setDrop(BlockState state, String drop){
            Boolean b = BlockPropertyUtils.BOOLEAN_MAP.get(drop);
            if(b == null) return state;
            return state.setValue(DROP_SELF, b);
        }

        @Info("BlockState state, boolean drop")
        public static BlockState setDrop(BlockState state, boolean drop){
            return state.setValue(DROP_SELF, drop);
        }

        @Info("arg0: BlockState state, arg1: String addEat \"${number}\", arg2: boolean drop")
        public static BlockState consume(BlockState state, String addEat, boolean drop){
            int eat = state.getValue(EAT);
            eat += Integer.parseInt(addEat);
            return state.setValue(DROP_SELF, drop).setValue(EAT, eat);
        }

        @Info("arg0: BlockState state, arg1: String addEat \"${number}\", arg2: boolean drop \"true\" | \"false\"")
        public static BlockState consume(BlockState state, String addEat, String drop){
            int eat = state.getValue(EAT);
            eat += Integer.parseInt(addEat);
            Boolean b = BlockPropertyUtils.BOOLEAN_MAP.get(drop);
            if(b == null) return state.setValue(EAT, eat);
            return state.setValue(DROP_SELF, b).setValue(EAT, eat);
        }

        @Info("arg0: BlockState state, arg1: String addEat \"${number}\", drop set false")
        public static BlockState consume(BlockState state, String addEat){
            int eat = state.getValue(EAT);
            eat += Integer.parseInt(addEat);
            return state.setValue(DROP_SELF, false).setValue(EAT, eat);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(FACING, DROP_SELF, EAT);
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
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof DecayingFoodBlockEntity decaying) {
                if (!Helpers.isBlock(state, newState.getBlock()) && entity.getBlockState().getValue(DROP_SELF)) {
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }
    }
}
