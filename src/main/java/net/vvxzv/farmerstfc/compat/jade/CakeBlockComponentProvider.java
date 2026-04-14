package net.vvxzv.farmerstfc.compat.jade;

import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.TFCCandleCakeBlock;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.food.Nutrient;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.vvxzv.farmerstfc.FarmersTFC;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public enum CakeBlockComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Block block = blockAccessor.getBlock();
        ItemStack stack;
        if (block instanceof TFCCandleCakeBlock) {
            stack = new ItemStack(TFCBlocks.CAKE.get());
        } else {
            stack = block.asItem().getDefaultInstance();
        }
        setTooltip(iTooltip, stack);
    }

    private void setTooltip(ITooltip iTooltip, ItemStack stack) {
        iTooltip.add(stack.getHoverName());
        List<Component> lines = new ArrayList<>();
        FoodDefinition food = FoodCapability.getDefinition(stack);
        if (food != null) {
            FoodData data = food.food();
            if (ClientHelpers.hasShiftDown()) {
                lines.add(Component.translatable("tfc.tooltip.nutrition").withStyle(ChatFormatting.GRAY));
                boolean any = false;
                float saturation = data.saturation();
                if (saturation > 0.0F) {
                    lines.add(Component.translatable("tfc.tooltip.nutrition_saturation", String.format("%d", (int)(saturation * 5.0F))).withStyle(ChatFormatting.GRAY));
                    any = true;
                }

                int water = (int)data.water();
                if (water > 0) {
                    lines.add(Component.translatable("tfc.tooltip.nutrition_water", String.format("%d", water)).withStyle(ChatFormatting.GRAY));
                    any = true;
                }

                for(Nutrient nutrient : Nutrient.VALUES) {
                    float value = data.nutrient(nutrient);
                    if (value > 0.0F) {
                        MutableComponent component = Component.literal(" - ").append(Helpers.translateEnum(nutrient));
                        lines.add(component.append(": " + String.format("%.1f", value)).withStyle(nutrient.getColor()));
                        any = true;
                    }
                }

                if (!any) {
                    lines.add(Component.translatable("tfc.tooltip.nutrition_none").withStyle(ChatFormatting.GRAY));
                }
            } else {
                lines.add(Component.translatable("tfc.tooltip.hold_shift_for_nutrition_info").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }
        }

        lines.forEach(iTooltip::add);
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "cake");
    }
}