package net.vvxzv.farmerstfc.compat.firmalife;

import com.eerussianguy.firmalife.common.blocks.FLBlocks;
import com.eerussianguy.firmalife.common.util.Mechanics;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.utils.FoodTraits;

import java.util.Set;

public class FLEventHandler {

    public static void init(IEventBus bus) {
        bus.addListener(FLEventHandler::placeBlockInCellar);
        bus.addListener(FLEventHandler::removeCellarFoodTrait);
        bus.addListener(FLEventHandler::breakFoodBlock);
    }

    public static void placeBlockInCellar(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if(state.is(FLBlocks.CLIMATE_STATION.get())) {
            Set<BlockPos> cellarPositions = Mechanics.getCellar(level, pos, state);
            if (cellarPositions != null) {
                cellarPositions.forEach(b -> {
                    if(level.getBlockEntity(b) instanceof DecayingFoodBlockEntity decay) {
                        ItemStack stack = decay.copyStack();
                        FoodCapability.applyTrait(stack, FoodTraits.CELLAR_PRESERVED);
                        decay.setStackWithCount(stack);
                    }
                });
            }
        }
    }

    public static void removeCellarFoodTrait(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if(level.getGameTime() % 20L == 0L) {
            player.getInventory().items.forEach(item -> {
                if(FoodCapability.get(item) != null) {
                    FoodCapability.removeTrait(item, FoodTraits.CELLAR_PRESERVED);
                }
            });
        }
    }

    public static void breakFoodBlock(BlockEvent.BreakEvent event) {
        if(event.getLevel().getBlockEntity(event.getPos()) instanceof DecayingFoodBlockEntity decay) {
            ItemStack stack = decay.copyStack();
            FoodCapability.removeTrait(stack, FoodTraits.CELLAR_PRESERVED);
            decay.setStackWithCount(stack);
        }
    }
}
