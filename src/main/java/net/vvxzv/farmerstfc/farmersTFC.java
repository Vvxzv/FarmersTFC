package net.vvxzv.farmerstfc;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.farmerstfc.common.registry.block;
import net.vvxzv.farmerstfc.common.registry.creativeTAB;
import net.vvxzv.farmerstfc.common.registry.item;
import org.slf4j.Logger;


@Mod(farmersTFC.MOD_ID)
public class farmersTFC {
    public static final String MOD_ID = "farmerstfc";
    private static final Logger LOGGER = LogUtils.getLogger();

    public farmersTFC() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        block.BLOCKS.register(modEventBus);
        item.ITEMS.register(modEventBus);
        creativeTAB.CREATIVE_MODE_TAB.register(modEventBus);
    }
}
