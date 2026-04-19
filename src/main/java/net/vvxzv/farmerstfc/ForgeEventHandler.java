package net.vvxzv.farmerstfc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.data.Crate;
import net.vvxzv.farmerstfc.common.data.DecayToRot;
import net.vvxzv.farmerstfc.compat.firmalife.FLEventHandler;

public class ForgeEventHandler {
    public static void init() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ForgeEventHandler::addReloadListeners);
        bus.addListener(ForgeEventHandler::placeCrateBlock);

        if(ModList.get().isLoaded("firmalife")) {
            FLEventHandler.init(bus);
        }
    }

    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(DecayToRot.MANAGER);
        event.addListener(Crate.MANAGER);
    }

    public static void placeCrateBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();

        if (player == null || !player.isShiftKeyDown()) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Direction face = event.getFace();
        if (face == null) {
            return;
        }

        BlockPos clickPos = event.getPos();
        BlockState clickedState = level.getBlockState(clickPos);
        BlockPos placePos = clickedState.canBeReplaced() ? clickPos : clickPos.relative(face);

        ItemStack stack = player.getMainHandItem();
        Crate crate = Crate.get(stack);

        if (crate != null) {
            Block block = crate.getBlock();
            BlockState state = block.defaultBlockState();

            if (!level.isUnobstructed(state, placePos, CollisionContext.of(player))) {
                return;
            }

            level.setBlockAndUpdate(placePos, state);

            SoundType soundtype = state.getSoundType(level, placePos, player);
            level.playSound(
                    player,
                    placePos,
                    soundtype.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (soundtype.getVolume() + 1.0F) / 2.0F,
                    soundtype.getPitch() * 0.8F
            );

            if (level.getBlockEntity(placePos) instanceof DecayingFoodBlockEntity decaying) {
                decaying.setStackWithCount(stack);
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            } else {
                stack.shrink(1);
            }

            event.setCanceled(true);
        }
    }
}
