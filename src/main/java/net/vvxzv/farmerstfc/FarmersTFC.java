package net.vvxzv.farmerstfc;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import net.vvxzv.farmerstfc.common.registry.Blocks;
import net.vvxzv.farmerstfc.common.registry.Items;
import net.vvxzv.farmerstfc.common.loot.LootModifiers;
import net.vvxzv.farmerstfc.common.utils.FoodTraits;
import net.vvxzv.farmerstfc.common.utils.Utils;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModCreativeTabs;

@Mod(FarmersTFC.MODID)
public class FarmersTFC {
    public static final String MODID = "farmerstfc";

    @SuppressWarnings("removal")
    public FarmersTFC() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Items.ITEMS.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        LootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::setup);

        ForgeEventHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeTabs.TAB_FARMERS_DELIGHT.get()) {
            Utils.replaceTabItem(event, ModBlocks.RICH_SOIL_FARMLAND.get(), Blocks.RICH_SOIL_FARMLAND.get());
            Utils.replaceTabItem(event, ModBlocks.CARROT_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.CARROT).get());
            Utils.replaceTabItem(event, ModBlocks.POTATO_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.POTATO).get());
            Utils.replaceTabItem(event, ModBlocks.BEETROOT_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.BEETROOT).get());
            Utils.replaceTabItem(event, ModBlocks.CABBAGE_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.CABBAGE).get());
            Utils.replaceTabItem(event, ModBlocks.TOMATO_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.TOMATO).get());
            Utils.replaceTabItem(event, ModBlocks.ONION_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.ONION).get());
            Utils.replaceTabItem(event, ModBlocks.RICE_BALE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.RICE_BALE).get());
            Utils.replaceTabItem(event, ModBlocks.RICE_BAG.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.RICE_BAG).get());

            event.accept(Blocks.PAN);
        }
    }

    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(FoodTraits::registerFoodTraits);
    }
}
