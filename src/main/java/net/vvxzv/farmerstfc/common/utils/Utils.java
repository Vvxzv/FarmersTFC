package net.vvxzv.farmerstfc.common.utils;

import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.food.*;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Utils {
    public static ItemStack copyFood(LevelAccessor level, BlockPos pos, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            ItemStack blockItem = decaying.copyStack();
            return copyFood(blockItem, stack);
        }
        return stack;
    }

    public static ItemStack copyFood(ItemStack input, ItemStack output) {
        IFood inputFood = FoodCapability.get(input);
        if (inputFood != null) {
            FoodComponent foodComponent = input.get(TFCComponents.FOOD);
            if (foodComponent != null) {
                output.set(TFCComponents.FOOD, foodComponent);
            }
        }
        return output;
    }

    public static final Map<String, Direction> DIRECTION_MAP = new HashMap<>();
    static {
        DIRECTION_MAP.put("north", Direction.NORTH);
        DIRECTION_MAP.put("south", Direction.SOUTH);
        DIRECTION_MAP.put("west", Direction.WEST);
        DIRECTION_MAP.put("east", Direction.EAST);
    }

    public static void replaceTabItem(BuildCreativeModeTabContentsEvent event, ItemLike oldItem, ItemLike newItem) {
        event.insertAfter(
                new ItemStack(oldItem),
                new ItemStack(newItem),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
        );
        event.remove(new ItemStack(oldItem), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    public static Optional<RecipeHolder<CampfireCookingRecipe>> heatingRecipeToCampfireCookingRecipe(HeatingRecipe heatingRecipe) {
        CampfireCookingRecipe fakeCookingRecipe = new CampfireCookingRecipe(
                heatingRecipe.getGroup(),
                CookingBookCategory.FOOD,
                heatingRecipe.getIngredient(),
                heatingRecipe.getResultItem(null),
                0,
                600
        );

        RecipeHolder<CampfireCookingRecipe> recipeHolder = new RecipeHolder<>(
                TFCRecipeTypes.HEATING.getId(),
                fakeCookingRecipe
        );

        return Optional.of(recipeHolder);
    }
}
