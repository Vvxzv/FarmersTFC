package net.vvxzv.farmerstfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = farmersTFC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue FERTILIZER_ON_RICH_FARMLAND =BUILDER.comment("对沃土耕地施肥的养分加成倍率").defineInRange("fertilizerOnRichFarmland", 2.5, 1, 10);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double fertilizerOnRichFarmland;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        fertilizerOnRichFarmland =FERTILIZER_ON_RICH_FARMLAND.get();
    }
}
