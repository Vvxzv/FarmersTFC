package net.vvxzv.farmerstfc.mixin;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    public void decayingMushroomItemPlace(BlockPlaceContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack handItem = pContext.getItemInHand();
        if(handItem.is(Tags.Items.MUSHROOMS)){
            IFood iFood = FoodCapability.get(handItem);
            if (iFood != null && iFood.isRotten()) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}
