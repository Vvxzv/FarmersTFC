package net.vvxzv.farmerstfc.common.utils;

import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.farmerstfc.FarmersTFC;

public class FoodTraits {
    public static void registerFoodTraits() {
    }

    private static FoodTrait register(String name, float decayModifier) {
        return FoodTrait.register(ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, name), new FoodTrait(decayModifier, "farmerstfc.tooltip.food_trait." + name));
    }

    public static final FoodTrait SKILLET_COOKED  = register("skillet_cooked", 0.9F);
    public static final FoodTrait CELLAR_PRESERVED = register("cellar_preserved", 0.5F);

}
