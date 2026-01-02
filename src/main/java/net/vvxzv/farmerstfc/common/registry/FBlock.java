package net.vvxzv.farmerstfc.common.registry;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
import net.vvxzv.farmerstfc.common.block.PanBlock;
import net.vvxzv.farmerstfc.common.block.RottenPieFoodBlock;
import net.vvxzv.farmerstfc.common.block.RottenPlateFoodBlock;
import net.vvxzv.farmerstfc.common.block.decay.*;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.blockEntity.FDecayingBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class FBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersTFC.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> blockObject = BLOCKS.register(name, block);
        FItem.ITEMS.register(name, () -> new BlockItem(blockObject.get(), new Item.Properties()));
        return blockObject;
    }

    private static <T extends Block> RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }

    private static ExtendedProperties decayingBlockProperties(){
        return ExtendedProperties.of(MapColor.COLOR_ORANGE).mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).blockEntity(FBlockEntity.DECAYING).serverTicks(FDecayingBlockEntity::serverTick).instrument(NoteBlockInstrument.DIDGERIDOO).pushReaction(PushReaction.DESTROY);
    }

    public static final RegistryObject<Block> BROWN_MUSHROOM_BUNCH = registerBlock("brown_mushroom_bunch", () -> new Block(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));

    public static final RegistryObject<Block> RED_MUSHROOM_BUNCH = registerBlock("red_mushroom_bunch", () -> new Block(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));

    public static final RegistryObject<Block> PAN = registerBlock("pan", () -> new PanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));

    public static final RegistryObject<Block> ROTTEN_ROAST_CHICKEN_BLOCK = registerBlockNoItem("rotten_roast_chicken_block", () -> new RottenPlateFoodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> ROAST_CHICKEN_BLOCK = registerBlock("roast_chicken_block", () -> new DecayingRoastChickenBlock(decayingBlockProperties(), ModItems.ROAST_CHICKEN, true, FBlock.ROTTEN_ROAST_CHICKEN_BLOCK));

    public static final RegistryObject<Block> STUFFED_PUMPKIN_BLOCK = registerBlock("stuffed_pumpkin_block", () -> new DecayingFeastBlock(decayingBlockProperties(), ModItems.STUFFED_PUMPKIN, false, TFCBlocks.ROTTEN_PUMPKIN));

    public static final RegistryObject<Block> ROTTEN_HONEY_GLAZED_HAM_BLOCK = registerBlockNoItem("rotten_honey_glazed_ham_block", () -> new RottenPlateFoodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> HONEY_GLAZED_HAM_BLOCK = registerBlock("honey_glazed_ham_block", () -> new DecayingHoneyGlazedHamBlock(decayingBlockProperties(), ModItems.HONEY_GLAZED_HAM, true, FBlock.ROTTEN_HONEY_GLAZED_HAM_BLOCK));

    public static final RegistryObject<Block> ROTTEN_SHEPHERDS_PIE_BLOCK = registerBlockNoItem("rotten_shepherds_pie_block", () -> new RottenPlateFoodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> SHEPHERDS_PIE_BLOCK = registerBlock("shepherds_pie_block", () -> new DecayingShepherdsPieBlock(decayingBlockProperties(), ModItems.SHEPHERDS_PIE, true, FBlock.ROTTEN_SHEPHERDS_PIE_BLOCK));

    public static final RegistryObject<Block> RICE_ROLL_MEDLEY_BLOCK = registerBlock("rice_roll_medley_block", () -> new DecayingRiceRollMedleyBlock(decayingBlockProperties(), TFCBlocks.WOODEN_BOWL));

    public static final RegistryObject<Block> ROTTEN_APPLE_PIE = registerBlockNoItem("rotten_apple_pie", () -> new RottenPieFoodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> APPLE_PIE = registerBlock("apple_pie", () -> new DecayingPieBlock(decayingBlockProperties(), ModItems.APPLE_PIE_SLICE, FBlock.ROTTEN_APPLE_PIE));

    public static final RegistryObject<Block> ROTTEN_SWEET_BERRY_CHEESECAKE = registerBlockNoItem("rotten_sweet_berry_cheesecake", () -> new RottenPieFoodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> SWEET_BERRY_CHEESECAKE = registerBlock("sweet_berry_cheesecake", () -> new DecayingPieBlock(decayingBlockProperties(), ModItems.SWEET_BERRY_CHEESECAKE_SLICE, FBlock.ROTTEN_SWEET_BERRY_CHEESECAKE));

    public static final RegistryObject<Block> ROTTEN_CHOCOLATE_PIE = registerBlockNoItem("rotten_chocolate_pie", () -> new RottenPieFoodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> CHOCOLATE_PIE = registerBlock("chocolate_pie", () -> new DecayingPieBlock(decayingBlockProperties(), ModItems.CHOCOLATE_PIE_SLICE, FBlock.ROTTEN_CHOCOLATE_PIE));

    public static final RegistryObject<Block> RICH_SOIL_FARMLAND = registerBlock("rich_soil_farmland",() -> new FarmlandBlock(ExtendedProperties.of(MapColor.DIRT).strength(1.3F).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(TFCBlockEntities.FARMLAND), ModBlocks.RICH_SOIL));
}
