package net.vvxzv.farmerstfc.common.utils;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static ItemStack copyFood(LevelAccessor level, BlockPos pos, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof DecayingFoodBlockEntity decaying) {
            ItemStack blockItem = decaying.copyStack();
            return copyFood(blockItem, stack);
        }
        return stack;
    }

    public static ItemStack copyFood(ItemStack input, ItemStack output) {
        IFood inputFood = FoodCapability.get(input);
        if(inputFood != null) {
            IFood outputFood = FoodCapability.get(output);
            if(outputFood != null) {
                outputFood.setCreationDate(inputFood.getCreationDate());
                List<FoodTrait> traits = inputFood.getTraits();
                for (FoodTrait trait : traits) {
                    FoodCapability.applyTrait(outputFood, trait);
                }
            }
        }
        return output;
    }

    public static final Map<String, Direction> DIRECTION_MAP = new HashMap<>();
    static {
        DIRECTION_MAP.put("north", Direction.NORTH);
        DIRECTION_MAP.put("south", Direction.SOUTH);
        DIRECTION_MAP.put("west", Direction.WEST);
        DIRECTION_MAP.put("east", Direction.EAST);
    }

    public static void replaceTabItem(BuildCreativeModeTabContentsEvent event, ItemLike oldItem, ItemLike newItem) {
        var entries = event.getEntries();
        var iterator = entries.iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getKey().is(oldItem.asItem())) {
                iterator.remove();
                entries.putBefore(entry.getKey(), new ItemStack(newItem), entry.getValue());
                break;
            }
        }
    }

    public static ResourceLocation getResourceLocation(String string) {
        String[] s = string.split(":");
        if(s.length == 2) {
            return ResourceLocation.fromNamespaceAndPath(s[0], s[1]);
        } else if(s.length == 1) {
            return ResourceLocation.fromNamespaceAndPath("minecraft" ,string);
        }

        throw new IllegalArgumentException("Unknow ResourceLocation: " + string);
    }
}
