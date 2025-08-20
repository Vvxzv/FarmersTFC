package net.vvxzv.farmerstfc.world.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.farmerstfc.farmersTFC;
import vectorwing.farmersdelight.common.registry.ModBlocks;


public class item {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> BROWN_MUSHROOM_BUNCH;
    public static final RegistryObject<Item> RED_MUSHROOM_BUNCH;
    public static final RegistryObject<Item> PAN;
    public static final RegistryObject<Item> ROAST_CHICKEN_BLOCK;
    public static final RegistryObject<Item> STUFFED_PUMPKIN_BLOCK;
    public static final RegistryObject<Item> HONEY_GLAZED_HAM_BLOCK;
    public static final RegistryObject<Item> SHEPHERDS_PIE_BLOCK;
    public static final RegistryObject<Item> RICE_ROLL_MEDLEY_BLOCK;
    public static final RegistryObject<Item> APPLE_PIE;
    public static final RegistryObject<Item> SWEET_BERRY_CHEESECAKE;
    public static final RegistryObject<Item> CHOCOLATE_PIE;

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, farmersTFC.MOD_ID);
        BROWN_MUSHROOM_BUNCH = ITEMS.register("brown_mushroom_bunch", () -> new BlockItem(block.BROWN_MUSHROOM_BUNCH.get(), basicItem()));
        RED_MUSHROOM_BUNCH = ITEMS.register("red_mushroom_bunch", () -> new BlockItem(block.RED_MUSHROOM_BUNCH.get(), basicItem()));
        PAN = ITEMS.register("pan", () -> new BlockItem(block.PAN.get(), basicItem()));
        ROAST_CHICKEN_BLOCK = ITEMS.register("roast_chicken_block", () -> new BlockItem(block.ROAST_CHICKEN_BLOCK.get(), basicItem()));
        STUFFED_PUMPKIN_BLOCK = ITEMS.register("stuffed_pumpkin_block", () -> new BlockItem(block.STUFFED_PUMPKIN_BLOCK.get(), basicItem()));
        HONEY_GLAZED_HAM_BLOCK = ITEMS.register("honey_glazed_ham_block", () -> new BlockItem(block.HONEY_GLAZED_HAM_BLOCK.get(), basicItem()));
        SHEPHERDS_PIE_BLOCK = ITEMS.register("shepherds_pie_block", () -> new BlockItem(block.SHEPHERDS_PIE_BLOCK.get(), basicItem()));
        RICE_ROLL_MEDLEY_BLOCK = ITEMS.register("rice_roll_medley_block", () -> new BlockItem(block.RICE_ROLL_MEDLEY_BLOCK.get(), basicItem()));
        APPLE_PIE = ITEMS.register("apple_pie", () -> new BlockItem(block.APPLE_PIE.get(), basicItem()));
        SWEET_BERRY_CHEESECAKE = ITEMS.register("sweet_berry_cheesecake", () -> new BlockItem(block.SWEET_BERRY_CHEESECAKE.get(), basicItem()));
        CHOCOLATE_PIE = ITEMS.register("chocolate_pie", () -> new BlockItem(block.CHOCOLATE_PIE.get(), basicItem()));
    }
}
