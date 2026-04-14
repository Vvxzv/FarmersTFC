package net.vvxzv.farmerstfc.common.registry;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.block.*;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class Blocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FarmersTFC.MODID);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> blockObject = BLOCKS.register(name, block);
        Items.ITEMS.register(name, () -> new BlockItem(blockObject.get(), new Item.Properties()));
        return blockObject;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }

    public static final DeferredBlock<Block> BROWN_MUSHROOM_BUNCH = registerBlock("brown_mushroom_bunch", () -> new MushroomBunchBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.TALL_GRASS)));

    public static final DeferredBlock<Block> RED_MUSHROOM_BUNCH = registerBlock("red_mushroom_bunch", () -> new MushroomBunchBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.TALL_GRASS)));

    public static final DeferredBlock<Block> RICH_SOIL_FARMLAND = registerBlock("rich_soil_farmland",() -> new RichSoilFarmLand(ExtendedProperties.of(MapColor.DIRT).strength(1.3F).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(BlockEntities.FARMLAND), ModBlocks.RICH_SOIL));

    public static final DeferredBlock<Block> PAN = registerBlock("pan", () -> new PanBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));

    public static final Map<RottenBlock, DeferredBlock<Block>> ROTTEN_BLOCK_MAP = Helpers.mapOf(
            RottenBlock.class,
            (block) -> registerBlockNoItem(
                    block.name().toLowerCase(Locale.ROOT),
                    () -> new RottenFoodBlock(block.getShape())
            )
    );

    public static final Map<RottenPie, DeferredBlock<Block>> ROTTEN_PIE_MAP = Helpers.mapOf(
            RottenPie.class,
            (block) -> registerBlockNoItem(block.name().toLowerCase(Locale.ROOT), RottenPieBlock::new)
    );

    public static final Map<Crate, DeferredBlock<Block>> CRATE_BLOCK_MAP = Helpers.mapOf(
            Crate.class,
            (block) -> registerBlock(block.getName(), CrateBlock::new)
    );

    public enum RottenBlock {
        ROTTEN_ROAST_CHICKEN_BLOCK,
        ROTTEN_STUFFED_PUMPKIN_BLOCK(Block.box(2.0F, 0.0F, 2.0F, 14.0F, 3.0F, 14.0F)),
        ROTTEN_HONEY_GLAZED_HAM_BLOCK,
        ROTTEN_SHEPHERDS_PIE_BLOCK;

        final VoxelShape shape;

        RottenBlock(){
            this(Block.box(1.0F, 0.0F, 1.0F, 15.0F, 2.0F, 15.0F));
        }
        RottenBlock(VoxelShape shape){
            this.shape = shape;
        }

        public VoxelShape getShape() {
            return shape;
        }
    }

    public enum RottenPie {
        ROTTEN_APPLE_PIE,
        ROTTEN_SWEET_BERRY_CHEESECAKE,
        ROTTEN_CHOCOLATE_PIE
    }

    public enum Crate {
        CARROT,
        POTATO,
        BEETROOT,
        CABBAGE,
        TOMATO,
        ONION,
        RICE_BALE(true),
        RICE_BAG(true);

        final String name;

        Crate() {
            this.name = this.name().toLowerCase(Locale.ROOT) + "_crate";
        }

        Crate(boolean isCustomName) {
            this.name = this.name().toLowerCase(Locale.ROOT);
        }

        public String getName() {
            return name;
        }
    }
}
