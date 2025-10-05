package net.vvxzv.farmerstfc.common.util;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class JEIUtil {
    private final IJeiRuntime jeiRuntime;

    public JEIUtil(IJeiRuntime jeiRuntime){
        this.jeiRuntime = jeiRuntime;
    }

    public void removeItemStack(ItemStack stack){
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Collections.singletonList(stack));
    }

    public void removeItemStack(Item item){
        this.removeItemStack(item.getDefaultInstance());
    }

    public void removeItemStack(Supplier<Item> reg){
        this.removeItemStack(reg.get().getDefaultInstance());
    }

    public <T extends Item> void removeItemStacks(List<T> items){
        items.forEach(this::removeItemStack);
    }
}
