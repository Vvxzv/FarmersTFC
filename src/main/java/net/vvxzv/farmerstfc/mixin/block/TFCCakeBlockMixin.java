package net.vvxzv.farmerstfc.mixin.block;

import net.dries007.tfc.common.blocks.TFCCakeBlock;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.player.IPlayerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
                    target = "Lnet/dries007/tfc/common/player/IPlayerInfo;eat(Lnet/dries007/tfc/common/component/food/FoodData;)V"
            )
    )
    private static void eatCake(IPlayerInfo instance, FoodData foodData, Level level, BlockPos pos, BlockState state, Player player) {
        ItemStack stack = state.getBlock().asItem().getDefaultInstance();
        FoodDefinition food = FoodCapability.getDefinition(stack);
        if(food != null) {
            instance.eat(food.food());
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof DecayingFoodBlockEntity decaying && decaying.isRotten()) {
            cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
    }
}
