package net.vvxzv.farmerstfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FarmersTFC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue FERTILIZER_ON_RICH_FARMLAND =BUILDER.comment(" ", "Nutrient addition ratio for fertilizing rich farmland", "对沃土耕地施肥的养分加成倍率", "2.5").defineInRange("fertilizerOnRichFarmland", 2.5, 1, 10);

    private static final ForgeConfigSpec.DoubleValue HEATING_TEMPERATURE = BUILDER.comment(" ", "The stove provides the highest temperature or the temperature of heating recipe suitable for skillet.", "烤炉提供群峦热源可达到的最高温度 或 煎锅适配加热配方的温度", "450.0").defineInRange("heatingTemperature", 450.0, 0, 2800);

    private static final ForgeConfigSpec.BooleanValue STOVE_NEEDS_FUEL = BUILDER.comment(" ", "The stove needs fuel to keep burning.", "炉灶需要燃料维持燃烧").define("stoveNeedsFuel", true);

    private static final ForgeConfigSpec.BooleanValue TRY_BOOSTING = BUILDER.comment(" ", "Rich soil will attempt to ripen the plants in the vanilla way","沃土会尝试用原版的方式催熟植物").define("tryBoosting", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double fertilizerOnRichFarmland;
    public static double heatingTemperature;
    public static boolean stoveNeedsFuel;
    public static boolean tryBoosting;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        fertilizerOnRichFarmland = FERTILIZER_ON_RICH_FARMLAND.get();
        heatingTemperature = HEATING_TEMPERATURE.get();
        stoveNeedsFuel = STOVE_NEEDS_FUEL.get();
        tryBoosting = TRY_BOOSTING.get();
    }
}
