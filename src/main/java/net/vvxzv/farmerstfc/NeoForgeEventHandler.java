package net.vvxzv.farmerstfc;

import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.data.Crate;
import net.vvxzv.farmerstfc.common.data.DataManagers;
import net.vvxzv.farmerstfc.compat.firmalife.FLEventHandler;

public class NeoForgeEventHandler {
    public static void init() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(NeoForgeEventHandler::addReloadListeners);
        bus.addListener(NeoForgeEventHandler::placeCrateBlock);

        if(ModList.get().isLoaded("firmalife")) {
            FLEventHandler.init(bus);
        }
    }

    public static void addReloadListeners(AddReloadListenerEvent event) {
        Registry<DataManager<?>> managers = DataManagers.REGISTRY;
        managers.forEach(event::addListener);
    }

    public static void placeCrateBlock(UseItemOnBlockEvent event) {
        Level level = event.getLevel();
        Player player = event.getPlayer();
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
        BlockPos placePos = clickPos.relative(face);
        if(level.getBlockState(clickPos).canBeReplaced()) {
            placePos = clickPos;
        }

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Block block = Crate.getBlock(stack);
        if (block != null) {
            BlockState state = block.defaultBlockState();

            if (!level.isUnobstructed(state, placePos, CollisionContext.of(player))) {
                return;
            }

            level.setBlockAndUpdate(placePos, state);
            SoundType soundtype = state.getSoundType(level, placePos, player);
            SoundEvent soundEvent = state.getSoundType(level, placePos, player).getPlaceSound();
            level.playSound(player, placePos, soundEvent, SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

            if (level.getBlockEntity(placePos) instanceof DecayingFoodBlockEntity decaying) {
                decaying.setStackWithCount(stack);
            }

            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

            event.setCanceled(true);
        }
    }
}
