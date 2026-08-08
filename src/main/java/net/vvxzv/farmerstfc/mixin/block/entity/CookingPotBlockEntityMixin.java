package net.vvxzv.farmerstfc.mixin.block.entity;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@Mixin(CookingPotBlockEntity.class)
public class CookingPotBlockEntityMixin {

    @Inject(method = "isContainerValid", at = @At("HEAD"), cancellable = true)
    private void isContainerValid(ItemStack containerItem, CallbackInfoReturnable<Boolean> cir) {
        IFood food = FoodCapability.get(containerItem);
        if(food != null && food.isRotten()) {
            cir.setReturnValue(false);
        }
    }
}
