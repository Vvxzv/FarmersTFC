package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.farmerstfc.FarmersTFC;

public class FCreativeTAB {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersTFC.MOD_ID);
    public static final RegistryObject<CreativeModeTab> FARMERSTFC;

    static {
        FARMERSTFC = CREATIVE_MODE_TAB.register("farmerstfc", () -> CreativeModeTab.builder()
                .title(Component.translatable("farmerstfc.tab.name"))
                .icon(() -> new ItemStack(FBlock.ROAST_CHICKEN_BLOCK.get()))
                .displayItems((parm, output) -> {
                    output.accept(FBlock.BROWN_MUSHROOM_BUNCH.get());
                    output.accept(FBlock.RED_MUSHROOM_BUNCH.get());
                    output.accept(FBlock.RICH_SOIL_FARMLAND.get());
                    output.accept(FBlock.PAN.get());
                    output.accept(FBlock.ROAST_CHICKEN_BLOCK.get());
                    output.accept(FBlock.STUFFED_PUMPKIN_BLOCK.get());
                    output.accept(FBlock.HONEY_GLAZED_HAM_BLOCK.get());
                    output.accept(FBlock.SHEPHERDS_PIE_BLOCK.get());
                    output.accept(FBlock.RICE_ROLL_MEDLEY_BLOCK.get());
                    output.accept(FBlock.APPLE_PIE.get());
                    output.accept(FBlock.SWEET_BERRY_CHEESECAKE.get());
                    output.accept(FBlock.CHOCOLATE_PIE.get());
                })
                .build()
        );
    }
}
