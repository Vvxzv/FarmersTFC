package net.vvxzv.farmerstfc.common.registry;

import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.farmerstfc.common.block.decay.*;
import net.vvxzv.farmerstfc.farmersTFC;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class block {
    public static final DeferredRegister<Block> BLOCKS;
    public static final RegistryObject<Block> BROWN_MUSHROOM_BUNCH;
    public static final RegistryObject<Block> RED_MUSHROOM_BUNCH;
    public static final RegistryObject<Block> PAN;
    public static final RegistryObject<Block> ROAST_CHICKEN_BLOCK;
    public static final RegistryObject<Block> STUFFED_PUMPKIN_BLOCK;
    public static final RegistryObject<Block> HONEY_GLAZED_HAM_BLOCK;
    public static final RegistryObject<Block> SHEPHERDS_PIE_BLOCK;
    public static final RegistryObject<Block> RICE_ROLL_MEDLEY_BLOCK;
    public static final RegistryObject<Block> APPLE_PIE;
    public static final RegistryObject<Block> SWEET_BERRY_CHEESECAKE;
    public static final RegistryObject<Block> CHOCOLATE_PIE;
    public static final RegistryObject<Block> RICH_SOIL_FARMLAND;

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, farmersTFC.MOD_ID);
        BROWN_MUSHROOM_BUNCH = BLOCKS.register("brown_mushroom_bunch", () -> new Block(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
        RED_MUSHROOM_BUNCH = BLOCKS.register("red_mushroom_bunch", () -> new Block(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
        PAN = BLOCKS.register("pan", () -> new SkilletBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
        ROAST_CHICKEN_BLOCK = BLOCKS.register("roast_chicken_block", () -> new DecayingRoastChickenBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.ROAST_CHICKEN, true, TFCBlocks.WOODEN_BOWL));
        STUFFED_PUMPKIN_BLOCK = BLOCKS.register("stuffed_pumpkin_block", () -> new DecayingFeastBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.STUFFED_PUMPKIN, false, TFCBlocks.ROTTEN_PUMPKIN));
        HONEY_GLAZED_HAM_BLOCK = BLOCKS.register("honey_glazed_ham_block", () -> new DecayingHoneyGlazedHamBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.HONEY_GLAZED_HAM, true, TFCBlocks.WOODEN_BOWL));
        SHEPHERDS_PIE_BLOCK = BLOCKS.register("shepherds_pie_block", () -> new DecayingShepherdsPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.SHEPHERDS_PIE, true, TFCBlocks.WOODEN_BOWL));
        RICE_ROLL_MEDLEY_BLOCK = BLOCKS.register("rice_roll_medley_block", () -> new DecayingRiceRollMedleyBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), TFCBlocks.WOODEN_BOWL));
        APPLE_PIE = BLOCKS.register("apple_pie", () -> new DecayingPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.APPLE_PIE_SLICE, block.APPLE_PIE));
        SWEET_BERRY_CHEESECAKE = BLOCKS.register("sweet_berry_cheesecake", () -> new DecayingPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.SWEET_BERRY_CHEESECAKE_SLICE, block.SWEET_BERRY_CHEESECAKE));
        CHOCOLATE_PIE = BLOCKS.register("chocolate_pie", () -> new DecayingPieBlock(ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(TFCBlockEntities.DECAYING).serverTicks(DecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY), ModItems.CHOCOLATE_PIE_SLICE, block.CHOCOLATE_PIE));
        RICH_SOIL_FARMLAND = BLOCKS.register("rich_soil_farmland",() -> new FarmlandBlock(ExtendedProperties.of(MapColor.DIRT).strength(1.3F).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(TFCBlockEntities.FARMLAND), ModBlocks.RICH_SOIL));
    }
}
