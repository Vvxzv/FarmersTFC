package net.vvxzv.farmerstfc.mixin.block.entity;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.utils.FoodTraits;
import net.vvxzv.farmerstfc.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

@Mixin(SkilletBlockEntity.class)
public class SkilletBlockEntityMixin extends SyncedBlockEntity {
    @Unique
    private static float skilletTemperature() {
        return (float) Config.heatingTemperature;
    }

    public SkilletBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(
            method = "getMatchingRecipe",
            at = @At("RETURN"),
            remap = false,
            cancellable = true
    )
    private void injectHeatingRecipe(ItemStack stack, CallbackInfoReturnable<Optional<RecipeHolder<CampfireCookingRecipe>>> cir) {
        if (FoodCapability.get(stack) != null) {
            HeatingRecipe matchingHeatingRecipe = HeatingRecipe.getRecipe(stack);
            if(matchingHeatingRecipe == null) {
                return;
            }

            if (matchingHeatingRecipe.getTemperature() > skilletTemperature()) {
                return;
            }

            CampfireCookingRecipe fakeCookingRecipe = new CampfireCookingRecipe(
                    matchingHeatingRecipe.getGroup(),
                    CookingBookCategory.FOOD,
                    matchingHeatingRecipe.getIngredient(),
                    matchingHeatingRecipe.getResultItem(null),
                    0,
                    600
            );

            RecipeHolder<CampfireCookingRecipe> recipeHolder = new RecipeHolder<>(
                    TFCRecipeTypes.HEATING.getId(),
                    fakeCookingRecipe
            );

            cir.setReturnValue(Optional.of(recipeHolder));
        }
    }

    @Redirect(
            method = "cookAndOutputItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lvectorwing/farmersdelight/common/utility/ItemUtils;spawnItemEntity(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;DDDDDD)V"
            ),
            remap = false
    )
    private void cookAndOutputItems(
            Level level,
            ItemStack stack,
            double x, double y, double z,
            double xMotion, double yMotion, double zMotion,
            ItemStack cookingStack
    ) {
        IHeat heat = HeatCapability.get(stack);
        if(heat != null) {
            heat.setTemperatureIfWarmer(skilletTemperature());
        }
        ItemStack outputItem = Utils.copyFood(cookingStack, stack);
        FoodCapability.applyTrait(outputItem, FoodTraits.SKILLET_COOKED);
        ItemUtils.spawnItemEntity(level, outputItem, x, y, z, xMotion, yMotion, zMotion);
    }
}
