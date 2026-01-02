package net.vvxzv.farmerstfc.compat.jei;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.registry.FItemTag;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class SkilletJEIPlugin implements IModPlugin {
    public static final RecipeType<HeatingRecipe> SKILLET_HEATING =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "skillet_heating"), HeatingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "jei_skillet");
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
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.getIngredient().getItems().length > 0 &&
                        recipe.getTemperature() < 201 &&
                        Arrays.stream(recipe.getIngredient().getItems()).noneMatch(stack -> stack.is(FItemTag.CANT_COOK))
                )
                .collect(Collectors.toList()).reversed();

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
