package net.vvxzv.farmerstfc.mixin;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.vvxzv.farmerstfc.FarmersTFC;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void decayingMushroomItemPlace(BlockPlaceContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack handItem = pContext.getItemInHand();
        if(handItem.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "cant_place_when_rotten")))){
            IFood food = FoodCapability.get(handItem);
            if (food != null && food.isRotten()) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}
