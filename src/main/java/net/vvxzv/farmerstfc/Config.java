package net.vvxzv.farmerstfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FarmersTFC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue FERTILIZER_ON_RICH_FARMLAND =BUILDER.comment(" ").comment("Nutrient addition ratio for fertilizing fertile farmland").comment("对沃土耕地施肥的养分加成倍率").defineInRange("fertilizerOnRichFarmland", 2.5, 1, 10);

    private static final ForgeConfigSpec.DoubleValue SKILLET_COOK_TEMPERATURE = BUILDER.comment(" ").comment("Skillet adapted heating recipe temperature (default 200)").comment("If you don't want the item to be added to the skillet for heating，add item tag #farmerstfc:cant_cook").comment("煎锅适配加热配方温度（默认200）").comment("如果不想让物品添加进煎锅加热，给物品添加#farmerstfc:cant_cook").defineInRange("skilletCookTemperature", 200.0, 200, 2000);

    private static final ForgeConfigSpec.DoubleValue STOVE_TEMPERATURE = BUILDER.comment(" ").comment("The stove provides the highest temperature that can be reached by a cluster of heat sources (default 450)").comment("烤炉提供群峦热源可达到的最高温度（默认450）").defineInRange("stoveTemperature", 450.0, 200, 2000);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double fertilizerOnRichFarmland;
    public static double skilletCookTemperature;
    public static double stoveTemperature;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        fertilizerOnRichFarmland = FERTILIZER_ON_RICH_FARMLAND.get();
        skilletCookTemperature = SKILLET_COOK_TEMPERATURE.get();
        stoveTemperature = STOVE_TEMPERATURE.get();
    }
}
