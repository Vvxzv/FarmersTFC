package net.vvxzv.farmerstfc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.vvxzv.farmerstfc.common.data.DataManagers;
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

    public FarmersTFC(IEventBus modEventBus, ModContainer modContainer) {
        Items.ITEMS.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        LootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        DataManagers.MANAGERS.register(modEventBus);
        FoodTraits.TRAITS.register(modEventBus);

        modEventBus.addListener(this::addCreativeTab);
        modEventBus.addListener(this::registerRegistries);

        NeoForgeEventHandler.init();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeTabs.TAB_FARMERS_DELIGHT.get()) {
            Utils.replaceTabItem(event, ModBlocks.RICH_SOIL_FARMLAND.get(), Blocks.RICH_SOIL_FARMLAND);
            Utils.replaceTabItem(event, ModBlocks.CARROT_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.CARROT));
            Utils.replaceTabItem(event, ModBlocks.POTATO_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.POTATO));
            Utils.replaceTabItem(event, ModBlocks.BEETROOT_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.BEETROOT));
            Utils.replaceTabItem(event, ModBlocks.CABBAGE_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.CABBAGE));
            Utils.replaceTabItem(event, ModBlocks.TOMATO_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.TOMATO));
            Utils.replaceTabItem(event, ModBlocks.ONION_CRATE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.ONION));
            Utils.replaceTabItem(event, ModBlocks.RICE_BALE.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.RICE_BALE));
            Utils.replaceTabItem(event, ModBlocks.RICE_BAG.get(), Blocks.CRATE_BLOCK_MAP.get(Blocks.Crate.RICE_BAG));

            event.accept(Blocks.PAN);
        }
    }

    public void registerRegistries(NewRegistryEvent event) {
        event.register(DataManagers.REGISTRY);
    }


}
