package net.vvxzv.farmerstfc.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.farmerstfc.farmersTFC;

public class creativeTAB {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, farmersTFC.MOD_ID);
    public static final RegistryObject<CreativeModeTab> FARMERSTFC;

    static {
        FARMERSTFC = CREATIVE_MODE_TAB.register("farmerstfc", () -> CreativeModeTab.builder()
                .title(Component.translatable("farmerstfc.tab.name"))
                .icon(() -> new ItemStack(block.ROAST_CHICKEN_BLOCK.get()))
                .displayItems((parm, output) -> {
                    output.accept(block.BROWN_MUSHROOM_BUNCH.get());
                    output.accept(block.RED_MUSHROOM_BUNCH.get());
                    output.accept(block.RICH_SOIL_FARMLAND.get());
                    output.accept(block.PAN.get());
                    output.accept(block.ROAST_CHICKEN_BLOCK.get());
                    output.accept(block.STUFFED_PUMPKIN_BLOCK.get());
                    output.accept(block.HONEY_GLAZED_HAM_BLOCK.get());
                    output.accept(block.SHEPHERDS_PIE_BLOCK.get());
                    output.accept(block.RICE_ROLL_MEDLEY_BLOCK.get());
                    output.accept(block.APPLE_PIE.get());
                    output.accept(block.SWEET_BERRY_CHEESECAKE.get());
                    output.accept(block.CHOCOLATE_PIE.get());
                })
                .build());
    }
}
