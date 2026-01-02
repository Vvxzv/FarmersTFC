package net.vvxzv.farmerstfc.mixin;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.HotCocoaItem;

@Mixin(HotCocoaItem.class)
public class HotCocoaItemMixin extends DrinkableItem {
    @Unique
    private static final FoodProperties HOT_COCOA = new FoodProperties.Builder().alwaysEat().build();

    public HotCocoaItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/item/DrinkableItem;<init>(Lnet/minecraft/world/item/Item$Properties;ZZ)V"))
    private static Properties hotCocoaInit(Properties properties){
        return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(HOT_COCOA);
    }
}
