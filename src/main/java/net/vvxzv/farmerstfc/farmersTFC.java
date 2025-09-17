package net.vvxzv.farmerstfc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.farmerstfc.common.registry.block;
import net.vvxzv.farmerstfc.common.registry.creativeTAB;
import net.vvxzv.farmerstfc.common.registry.item;


@Mod(farmersTFC.MOD_ID)
public class farmersTFC {
    public static final String MOD_ID = "farmerstfc";

    public farmersTFC() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        block.BLOCKS.register(modEventBus);
        item.ITEMS.register(modEventBus);
        creativeTAB.CREATIVE_MODE_TAB.register(modEventBus);
    }
}
