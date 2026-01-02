package net.vvxzv.farmerstfc;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.farmerstfc.common.registry.FBlock;
import net.vvxzv.farmerstfc.common.registry.FBlockEntity;
import net.vvxzv.farmerstfc.common.registry.FCreativeTAB;
import net.vvxzv.farmerstfc.common.registry.FItem;
import org.slf4j.Logger;


@Mod(FarmersTFC.MOD_ID)
public class FarmersTFC {
    public static final String MOD_ID = "farmerstfc";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public FarmersTFC() {
        LOGGER.info("Initializing Farmers TFC");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FBlock.BLOCKS.register(modEventBus);
        FItem.ITEMS.register(modEventBus);
        FCreativeTAB.CREATIVE_MODE_TAB.register(modEventBus);
        FBlockEntity.BLOCK_ENTITIES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
