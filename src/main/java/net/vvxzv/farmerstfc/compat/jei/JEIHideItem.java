package net.vvxzv.farmerstfc.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.utils.JEIUtil;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;

@JeiPlugin
public class JEIHideItem implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "hide_item");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime){
        JEIUtil util = new JEIUtil(jeiRuntime);
        util.removeItemStacks(List.of(
                ModItems.ROAST_CHICKEN_BLOCK.get(),
                ModItems.HONEY_GLAZED_HAM_BLOCK.get(),
                ModItems.STUFFED_PUMPKIN_BLOCK.get(),
                ModItems.SHEPHERDS_PIE_BLOCK.get(),
                ModItems.RICE_ROLL_MEDLEY_BLOCK.get(),
                ModItems.APPLE_PIE.get(),
                ModItems.SWEET_BERRY_CHEESECAKE.get(),
                ModItems.CHOCOLATE_PIE.get(),
                ModItems.RICH_SOIL_FARMLAND.get()
        ));
    }
}