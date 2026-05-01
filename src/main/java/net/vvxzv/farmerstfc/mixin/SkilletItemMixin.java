package net.vvxzv.farmerstfc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.utils.FoodTraits;
import net.vvxzv.farmerstfc.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SkilletItem.class)
public class SkilletItemMixin {
    @Unique
    private static float skilletTemperature(){
        return (float) Config.heatingTemperature + 1;
    }

    @Inject(method = "isPlayerNearHeatSource", at = @At(value = "RETURN", ordinal = 1), cancellable = true, remap = false)
    private static void isPlayerNearHeatSource(Player player, LevelReader level, CallbackInfoReturnable<Boolean> cir, @Local(name = "nearbyPos") BlockPos nearbyPos) {
        BlockState state = level.getBlockState(nearbyPos);
        if(state.hasProperty(BlockStateProperties.LIT)) {
            if(!state.getValue(BlockStateProperties.LIT)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "getCookingRecipe", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getHeatingRecipe(ItemStack stack, Level level, CallbackInfoReturnable<Optional<RecipeHolder<CampfireCookingRecipe>>> cir) {
        if(!stack.isEmpty()) {
            if (FoodCapability.get(stack) != null) {
                HeatingRecipe matchingHeatingRecipe = HeatingRecipe.getRecipe(stack);
                if(matchingHeatingRecipe != null) {
                    cir.setReturnValue(Utils.heatingRecipeToCampfireCookingRecipe(matchingHeatingRecipe));
                }
            }
        }
    }

    @Redirect(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
    private void finishUsingItem(Optional instance, Consumer action, ItemStack stack, Level level, LivingEntity entity) {
        if(entity instanceof Player player) {
            ItemStackWrapper storedStack = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT, ItemStackWrapper.EMPTY);
            ItemStack cookingStack = storedStack.getStack();
            Optional<RecipeHolder<CampfireCookingRecipe>> cookingRecipe = SkilletItem.getCookingRecipe(cookingStack, level);
            if(cookingRecipe.isPresent()) {
                ItemStack resultStack = cookingRecipe.get().value().assemble(new SingleRecipeInput(cookingStack), level.registryAccess());

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
