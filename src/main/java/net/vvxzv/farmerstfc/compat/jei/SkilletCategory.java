package net.vvxzv.farmerstfc.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.compat.jei.JEIIntegration;
import net.dries007.tfc.compat.jei.category.BaseRecipeCategory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.config.TemperatureDisplayStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Arrays;
import java.util.List;

public class SkilletCategory extends BaseRecipeCategory<HeatingRecipe> {
    public SkilletCategory(RecipeType<HeatingRecipe> type, IGuiHelper helper) {
        super(type, helper, 120, 38, new ItemStack(ModBlocks.SKILLET.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HeatingRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 21, 17);
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 17);
        inputSlot.addIngredients(recipe.getIngredient());
        inputSlot.setBackground(this.slot, -1, -1);
        List<ItemStack> outputItems = Arrays.stream(recipe.getIngredient().getItems()).map((stack) -> recipe.assembleStacked(stack, Integer.MAX_VALUE, true)).toList();
        FluidStack resultFluid = recipe.getDisplayOutputFluid();
        if (!outputItems.isEmpty() && !outputItems.stream().allMatch(ItemStack::isEmpty)) {
            outputSlot.addItemStacks(outputItems);
        }

        if (!resultFluid.isEmpty()) {
            outputSlot.addIngredient(JEIIntegration.FLUID_STACK, resultFluid);
            outputSlot.setFluidRenderer(1L, false, 16, 16);
        }

        outputSlot.setBackground(this.slot, -1, -1);
    }

    @Override
    public void draw(HeatingRecipe recipe, IRecipeSlotsView recipeSlots, GuiGraphics graphics, double mouseX, double mouseY) {
        this.fire.draw(graphics, 54, 16);
        this.fireAnimated.draw(graphics, 54, 16);
        MutableComponent color = ((TemperatureDisplayStyle)TFCConfig.CLIENT.heatTooltipStyle.get()).formatColored(recipe.getTemperature());
        if (color != null) {
            Minecraft mc = Minecraft.getInstance();
            Font font = mc.font;
            graphics.drawString(font, color, 60 - font.width(color) / 2, 4, 16777215, true);
        }

        for(IRecipeSlotView view : recipeSlots.getSlotViews()) {
            view.getDisplayedItemStack().ifPresent((stack) -> HeatCapability.setTemperature(stack, recipe.getTemperature()));
        }

    }
}
