package net.vvxzv.farmerstfc.mixin;

import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.common.registry.itemTagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Optional;

@Mixin(SkilletBlockEntity.class)
public abstract class SkilletBlockEntityMixin extends SyncedBlockEntity {
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
        if (cir.getReturnValue().isPresent()) {
            return;
        }

        if (stack.is(itemTagKey.CANT_COOK)) {
            return;
        }

        HeatingRecipe matchingHeatingRecipe = HeatingRecipe.getRecipe(stack);

        if (matchingHeatingRecipe.getTemperature() >= 201) {
            return;
        }

        CampfireCookingRecipe fakeCookingRecipe = new CampfireCookingRecipe(
                matchingHeatingRecipe.getGroup(),
                CookingBookCategory.FOOD,
                matchingHeatingRecipe.getIngredient(),
                matchingHeatingRecipe.getResultItem(level.registryAccess()),
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
