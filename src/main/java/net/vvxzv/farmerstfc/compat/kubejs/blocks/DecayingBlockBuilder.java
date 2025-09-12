package net.vvxzv.farmerstfc.compat.kubejs.blocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DecayingBlock;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DecayingBlockBuilder extends BlockBuilder {
    private static final Consumer<LootBuilder> EMPTY = (loot) -> {
    };
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
    public void generateDataJsons(DataJsonGenerator generator) {
        if (this.lootTable != null && this.lootTable != EMPTY) {
            LootBuilder lootBuilder = new LootBuilder((JsonElement) null);
            lootBuilder.type = "minecraft:block";

            this.lootTable.accept(lootBuilder);

            JsonObject json = lootBuilder.toJson();
            generator.json(this.newID("loot_tables/blocks/", ""), json);
        }
    }

    @Override
    public @NotNull Block createObject(){
        Supplier<? extends Block> rottedBlockSupplier = getSupplier();

        ExtendedProperties properties = ExtendedProperties.of(MapColor.COLOR_ORANGE)
                .mapColor(MapColor.COLOR_GREEN)
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .blockEntity(TFCBlockEntities.DECAYING)
                .serverTicks(DecayingBlockEntity::serverTick)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .pushReaction(PushReaction.DESTROY);

        return new DecayingBlockJS(properties, rottedBlockSupplier, dropSelf, customShape, eat);
    }

    private @NotNull Supplier<? extends Block> getSupplier() {
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

    public static class DecayingBlockJS extends DecayingBlock{
        public static final DirectionProperty FACING;
        public static final BooleanProperty DROP_SELF;
        public static final IntegerProperty EAT;
        private final VoxelShape combinedShape;

        public DecayingBlockJS(ExtendedProperties properties, Supplier<? extends Block> rotted, Boolean dropSelf, List<AABB> customShapes, int pEat) {
            super(properties, rotted);
            int eat1 = Math.min(pEat, 15);

            BlockState state = this.stateDefinition.any()
                    .setValue(FACING, Direction.NORTH)
                    .setValue(DROP_SELF, dropSelf)
                    .setValue(EAT, eat1);

            this.registerDefaultState(state);

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

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(FACING, DROP_SELF, EAT);
        }

        static {
            FACING = BlockStateProperties.HORIZONTAL_FACING;
            DROP_SELF = BooleanProperty.create("dropself");
            EAT = IntegerProperty.create("eat", 0, 15);
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
            if (entity instanceof DecayingBlockEntity decaying) {
                if (!Helpers.isBlock(state, newState.getBlock()) && entity.getBlockState().getValue(DROP_SELF)) {
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }
    }
}