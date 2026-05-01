package net.vvxzv.farmerstfc.mixin;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.utils.FoodTraits;
import net.vvxzv.farmerstfc.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.item.SkilletItem;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SkilletItem.class)
public class SkilletItemMixin {
    @Unique
    private static float skilletTemperature(){
        return (float) Config.heatingTemperature + 1;
    }

    @Inject(method = "isPlayerNearHeatSource", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, remap = false)
    private static void isPlayerNearHeatSource(Player player, LevelReader level, CallbackInfoReturnable<Boolean> cir, BlockPos pos, Iterator var3, BlockPos nearbyPos) {
        BlockState state = level.getBlockState(nearbyPos);
        if(state.hasProperty(BlockStateProperties.LIT)) {
            if(!state.getValue(BlockStateProperties.LIT)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "getCookingRecipe", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getHeatingRecipe(ItemStack stack, Level level, CallbackInfoReturnable<Optional<CampfireCookingRecipe>> cir) {
        if(!stack.isEmpty()) {
            if (FoodCapability.get(stack) != null) {
                cir.setReturnValue(Utils.heatingRecipeToCampfireCookingRecipe(level, stack));
            }
        }
    }

    @Redirect(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
    private void finishUsingItem(Optional instance, Consumer action, ItemStack stack, Level level, LivingEntity entity) {
        if(entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
            Optional<CampfireCookingRecipe> cookingRecipe = SkilletItem.getCookingRecipe(cookingStack, level);
            if(cookingRecipe.isPresent()) {
                CampfireCookingRecipe recipe = cookingRecipe.get();
                ItemStack resultStack = recipe.assemble(new SimpleContainer(), level.registryAccess());
                ItemStack outputItem = Utils.copyFood(cookingStack, resultStack);
                FoodCapability.applyTrait(outputItem, FoodTraits.SKILLET_COOKED);
                IHeat heat = HeatCapability.get(outputItem);
                if(heat != null) {
                    heat.setTemperature(skilletTemperature());
                }
                if (!player.getInventory().add(resultStack)) {
                    player.drop(resultStack, false);
                }

                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
                }
            }
        }
    }
}
