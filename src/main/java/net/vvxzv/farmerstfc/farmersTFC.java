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
import net.vvxzv.farmerstfc.world.registry.block;
import net.vvxzv.farmerstfc.world.registry.blockEntity;
import net.vvxzv.farmerstfc.world.registry.creativeTAB;
import net.vvxzv.farmerstfc.world.registry.item;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(farmersTFC.MOD_ID)
public class farmersTFC {
    public static final String MOD_ID = "farmerstfc";
    private static final Logger LOGGER = LogUtils.getLogger();



    public farmersTFC() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        block.BLOCKS.register(modEventBus);
        item.ITEMS.register(modEventBus);
        creativeTAB.CREATIVE_MODE_TAB.register(modEventBus);
        blockEntity.BLOCK_ENTITIES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }
    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
