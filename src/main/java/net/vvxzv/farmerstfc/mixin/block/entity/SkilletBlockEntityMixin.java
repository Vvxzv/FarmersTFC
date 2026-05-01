package net.vvxzv.farmerstfc.mixin.block.entity;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.HeatHandler;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

@Mixin(value = SkilletBlockEntity.class, remap = false)
public abstract class SkilletBlockEntityMixin extends SyncedBlockEntity {
    @Unique
    private float skilletTemperature(){
        return (float) Config.heatingTemperature + 1;
    }

    public SkilletBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(
            method = "getMatchingRecipe",
            at = @At("HEAD"),
            cancellable = true
    )
    private void injectHeatingRecipe(Container recipeWrapper, CallbackInfoReturnable<Optional<CampfireCookingRecipe>> cir) {
        if(this.level != null) {
            ItemStack inputStack = recipeWrapper.getItem(0);
            if (FoodCapability.get(inputStack) != null) {
                cir.setReturnValue(Utils.heatingRecipeToCampfireCookingRecipe(this.level, inputStack));
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