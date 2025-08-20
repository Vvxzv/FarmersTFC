package net.vvxzv.farmerstfc.compact.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vvxzv.farmerstfc.farmersTFC;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@JeiPlugin
public class SkilletJEIPlugin implements IModPlugin {
    public static final RecipeType<HeatingRecipe> SKILLET_HEATING =
            new RecipeType<>(new ResourceLocation(farmersTFC.MOD_ID, "skillet_heating"), HeatingRecipe.class);

    private static final TagKey<Item> CANT_COOK_TAG = TagKey.create(
            net.minecraft.core.registries.Registries.ITEM,
            new ResourceLocation("farmerstfc", "cant_cook")
    );

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(farmersTFC.MOD_ID, "jei_skillet");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SkilletCategory(
                        SKILLET_HEATING,
                        registration.getJeiHelpers().getGuiHelper()
                )
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<HeatingRecipe> recipes = ClientHelpers.getLevelOrThrow()
                .getRecipeManager()
                .getAllRecipesFor(TFCRecipeTypes.HEATING.get())
                .stream()
                .filter(recipe -> {
                    return recipe.getIngredient().getItems().length > 0 &&
                            recipe.getTemperature() < 201 &&
                            Arrays.stream(recipe.getIngredient().getItems())
                                    .noneMatch(stack -> stack.is(CANT_COOK_TAG));
                })
                .collect(Collectors.toList());

        registration.addRecipes(SKILLET_HEATING, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.SKILLET.get()),
                SKILLET_HEATING
        );
    }
}