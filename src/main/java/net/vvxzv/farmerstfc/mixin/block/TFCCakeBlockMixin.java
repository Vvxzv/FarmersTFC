package net.vvxzv.farmerstfc.mixin.block;

import net.dries007.tfc.common.blocks.TFCCakeBlock;
import net.dries007.tfc.common.capabilities.food.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TFCCakeBlock.class)
public class TFCCakeBlockMixin {
    @Redirect(
            method = "eatCake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/common/capabilities/food/TFCFoodData;eat(Lnet/dries007/tfc/common/capabilities/food/FoodData;)V"
            ),
            remap = false
    )
    private static void eatCake(TFCFoodData instance, FoodData foodData, Level level, BlockPos pos, BlockState state, Player player) {
        ItemStack stack = state.getBlock().asItem().getDefaultInstance();
        FoodDefinition food = FoodCapability.getDefinition(stack);
        if(food != null) {
            instance.eat(food.getData());
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
