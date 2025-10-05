package net.vvxzv.farmerstfc.common.registry;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.block.PanBlock;
import net.vvxzv.farmerstfc.common.block.RichSoilFarmLand;
import net.vvxzv.farmerstfc.common.block.decay.*;
import net.vvxzv.farmerstfc.common.blockEntity.FDecayingBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class block {
    public static final DeferredRegister.Blocks BLOCKS;
    public static final DeferredBlock<Block> BROWN_MUSHROOM_BUNCH;
    public static final DeferredBlock<Block> RED_MUSHROOM_BUNCH;
    public static final DeferredBlock<Block> RICH_SOIL_FARMLAND;
    public static final DeferredBlock<Block> PAN;
    public static final DeferredBlock<Block> ROAST_CHICKEN_BLOCK;
    public static final DeferredBlock<Block> STUFFED_PUMPKIN_BLOCK;
    public static final DeferredBlock<Block> HONEY_GLAZED_HAM_BLOCK;
    public static final DeferredBlock<Block> SHEPHERDS_PIE_BLOCK;
    public static final DeferredBlock<Block> RICE_ROLL_MEDLEY_BLOCK;
    public static final DeferredBlock<Block> APPLE_PIE;
    public static final DeferredBlock<Block> SWEET_BERRY_CHEESECAKE;
    public static final DeferredBlock<Block> CHOCOLATE_PIE;

    static {
        BLOCKS = DeferredRegister.createBlocks(FarmersTFC.MODID);
        BROWN_MUSHROOM_BUNCH = registerBlock("brown_mushroom_bunch", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)));
        RED_MUSHROOM_BUNCH = registerBlock("red_mushroom_bunch", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)));
        RICH_SOIL_FARMLAND = registerBlock("rich_soil_farmland",() -> new RichSoilFarmLand(ExtendedProperties.of(MapColor.DIRT).strength(1.3F).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(blockEntities.FARMLAND), ModBlocks.RICH_SOIL));
        PAN = registerBlock("pan", () -> new PanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
        ROAST_CHICKEN_BLOCK = registerBlock("roast_chicken_block", () -> new DecayingRoastChickenBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.ROAST_CHICKEN, true, TFCBlocks.WOODEN_BOWL));
        STUFFED_PUMPKIN_BLOCK = registerBlock("stuffed_pumpkin_block", () -> new DecayingFeastBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.STUFFED_PUMPKIN, false, TFCBlocks.ROTTEN_PUMPKIN));
        HONEY_GLAZED_HAM_BLOCK = registerBlock("honey_glazed_ham_block", () -> new DecayingHoneyGlazedHamBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.HONEY_GLAZED_HAM, true, TFCBlocks.WOODEN_BOWL));
        SHEPHERDS_PIE_BLOCK = registerBlock("shepherds_pie_block", () -> new DecayingShepherdsPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.SHEPHERDS_PIE, true, TFCBlocks.WOODEN_BOWL));
        RICE_ROLL_MEDLEY_BLOCK = registerBlock("rice_roll_medley_block", () -> new DecayingRiceRollMedleyBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), TFCBlocks.WOODEN_BOWL));
        APPLE_PIE = registerBlock("apple_pie", () -> new DecayingPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.APPLE_PIE_SLICE, block.APPLE_PIE));
        SWEET_BERRY_CHEESECAKE = registerBlock("sweet_berry_cheesecake", () -> new DecayingPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.SWEET_BERRY_CHEESECAKE_SLICE, block.SWEET_BERRY_CHEESECAKE));
        CHOCOLATE_PIE = registerBlock("chocolate_pie", () -> new DecayingPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(blockEntities.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.CHOCOLATE_PIE_SLICE, block.CHOCOLATE_PIE));
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        item.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItem(name, blocks);
        return blocks;
    }
}
