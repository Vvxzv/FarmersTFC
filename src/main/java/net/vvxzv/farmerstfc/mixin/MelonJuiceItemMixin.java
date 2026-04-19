package net.vvxzv.farmerstfc.mixin;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.MelonJuiceItem;

@Mixin(MelonJuiceItem.class)
public class MelonJuiceItemMixin extends DrinkableItem {
    @Unique
    private static final FoodProperties MELON_JUICE = new FoodProperties.Builder().alwaysEat().build();

    public MelonJuiceItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/item/DrinkableItem;<init>(Lnet/minecraft/world/item/Item$Properties;ZZ)V"))
    private static Properties melonJuiceInit(Properties properties) {
        return new Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(MELON_JUICE);
    }

}
