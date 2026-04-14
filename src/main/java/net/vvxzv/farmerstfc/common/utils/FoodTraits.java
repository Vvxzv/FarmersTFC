package net.vvxzv.farmerstfc.common.utils;

import net.dries007.tfc.common.component.food.FoodTrait;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;

import java.util.function.Supplier;

public class FoodTraits {
    public static final DeferredRegister<FoodTrait> TRAITS = DeferredRegister.create(net.dries007.tfc.common.component.food.FoodTraits.KEY, FarmersTFC.MODID);

    private static DeferredHolder<FoodTrait, FoodTrait> register(String name, Supplier<Double> decayModifier) {
        return TRAITS.register(name, () -> new FoodTrait(decayModifier, "farmerstfc.tooltip.food_trait." + name));
    }

    public static final DeferredHolder<FoodTrait, FoodTrait> SKILLET_COOKED = register("skillet_cooked", () -> 0.9);
    public static final DeferredHolder<FoodTrait, FoodTrait> CELLAR_PRESERVED = register("cellar_preserved", () -> 0.5);
}
