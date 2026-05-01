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

import java.util.Optional;

@Mixin(SkilletBlockEntity.class)
public class SkilletBlockEntityMixin extends SyncedBlockEntity {
    @Unique
    private static float skilletTemperature(){
        return (float) Config.heatingTemperature + 1;
    }

    public SkilletBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(
            method = "getMatchingRecipe",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void injectHeatingRecipe(ItemStack stack, CallbackInfoReturnable<Optional<RecipeHolder<CampfireCookingRecipe>>> cir) {
        if (FoodCapability.get(stack) != null) {
            HeatingRecipe matchingHeatingRecipe = HeatingRecipe.getRecipe(stack);
            if(matchingHeatingRecipe != null) {
                cir.setReturnValue(Utils.heatingRecipeToCampfireCookingRecipe(matchingHeatingRecipe));
            }
        }
    }

    @Redirect(
            method = "cookAndOutputItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack cooked(ItemStack stack, ItemStack cookingStack) {
        ItemStack outputItem = Utils.copyFood(cookingStack, stack);
        FoodCapability.applyTrait(outputItem, FoodTraits.SKILLET_COOKED);
        IHeat heat = HeatCapability.get(outputItem);
        if(heat != null) {
            heat.setTemperature(skilletTemperature());
        }
        return outputItem;
    }
}
