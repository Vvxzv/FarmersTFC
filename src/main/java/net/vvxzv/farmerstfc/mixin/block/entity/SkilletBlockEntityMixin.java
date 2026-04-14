package net.vvxzv.farmerstfc.mixin.block.entity;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.Level;
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
import vectorwing.farmersdelight.common.utility.ItemUtils;

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
        if(this.level == null) {
            return;
        }

        ItemStack inputStack = recipeWrapper.getItem(0);
        if (FoodCapability.get(inputStack) == null) {
            return;
        }
        Optional<HeatingRecipe> heatingRecipe = this.level.getRecipeManager()
                .getRecipeFor(TFCRecipeTypes.HEATING.get(), new ItemStackInventory(inputStack), this.level);

        if (heatingRecipe.isPresent()) {
            HeatingRecipe recipe = heatingRecipe.get();
            if (recipe.getTemperature() < this.skilletTemperature()) {
                CampfireCookingRecipe fakeCampfireRecipe = new CampfireCookingRecipe(
                        recipe.getId(),
                        "",
                        CookingBookCategory.FOOD,
                        recipe.getIngredient(),
                        recipe.assemble(new ItemStackInventory(inputStack), this.level.registryAccess()),
                        0,
                        600
                );

                cir.setReturnValue(Optional.of(fakeCampfireRecipe));
            }
        }
    }

    @Redirect(
            method = "cookAndOutputItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lvectorwing/farmersdelight/common/utility/ItemUtils;spawnItemEntity(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;DDDDDD)V"
            )
    )
    private void cookAndOutputItems(
            Level level,
            ItemStack stack,
            double x, double y, double z,
            double xMotion, double yMotion, double zMotion,
            ItemStack cookingStack
    ) {
        IHeat heat = HeatCapability.get(stack);
        if(heat != null) {
            heat.setTemperatureIfWarmer(skilletTemperature());
        }
        SimpleContainer wrapper = new SimpleContainer(cookingStack);
        ItemStack inputStack = wrapper.getItem(0);
        ItemStack outputItem = Utils.copyFood(inputStack, stack);
        FoodCapability.applyTrait(outputItem, FoodTraits.SKILLET_COOKED);
        ItemUtils.spawnItemEntity(level, outputItem, x, y, z, xMotion, yMotion, zMotion);
    }
}