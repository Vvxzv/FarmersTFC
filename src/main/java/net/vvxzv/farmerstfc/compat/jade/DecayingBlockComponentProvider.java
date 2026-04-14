package net.vvxzv.farmerstfc.compat.jade;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum DecayingBlockComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        if (blockEntity instanceof DecayingFoodBlockEntity decay) {
            ItemStack stack = decay.getStack();
            if (!stack.isEmpty()) {
                iTooltip.add(stack.getHoverName());
                FoodCapability.addTooltipInfo(stack, iTooltip::add);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "decaying");
    }
}