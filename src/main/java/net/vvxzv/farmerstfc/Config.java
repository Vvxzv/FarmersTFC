package net.vvxzv.farmerstfc;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = FarmersTFC.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue FERTILIZER_ON_RICH_FARMLAND = BUILDER.comment(" ", "Nutrient addition ratio for fertilizing rich farmland", "对沃土耕地施肥的养分加成倍率").defineInRange("fertilizerOnRichFarmland", 2.5, 1, 10);

    private static final ModConfigSpec.DoubleValue HEATING_TEMPERATURE = BUILDER.comment(" ", "The stove provides the highest temperature or the temperature of heating recipe suitable for skillet.", "烤炉提供群峦热源可达到的最高温度 或 煎锅适配加热配方的温度").defineInRange("stoveTemperature", 450.0, 200, 2000);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static double fertilizerOnRichFarmland;
    public static double heatingTemperature;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        fertilizerOnRichFarmland = FERTILIZER_ON_RICH_FARMLAND.get();
        heatingTemperature = HEATING_TEMPERATURE.get();
    }
}
