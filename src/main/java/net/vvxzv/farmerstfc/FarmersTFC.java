package net.vvxzv.farmerstfc;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.vvxzv.farmerstfc.common.registry.FBlock;
import net.vvxzv.farmerstfc.common.registry.FBlockEntity;
import net.vvxzv.farmerstfc.common.registry.FItem;
import org.slf4j.Logger;
import vectorwing.farmersdelight.common.registry.ModCreativeTabs;

@Mod(FarmersTFC.MODID)
public class FarmersTFC {
    public static final String MODID = "farmerstfc";
    public static final Logger LOGGER = LogUtils.getLogger();
    public FarmersTFC(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Initializing Farmers TFC");

        FBlock.BLOCKS.register(modEventBus);
        FItem.ITEMS.register(modEventBus);
        FBlockEntity.BLOCK_ENTITIES.register(modEventBus);
        modEventBus.addListener(this::addCreativeTab);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == ModCreativeTabs.TAB_FARMERS_DELIGHT.get()){
            FItem.ITEMS.getEntries().forEach(item -> {
                event.accept(item.get());
            });
        }
    }
}
