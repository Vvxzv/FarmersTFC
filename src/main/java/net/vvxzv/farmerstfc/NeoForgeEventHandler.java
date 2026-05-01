package net.vvxzv.farmerstfc;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.plant.fruit.FruitTreeSaplingBlock;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.common.block.entity.StoveBlockEntity;
import net.vvxzv.farmerstfc.common.data.Crate;
import net.vvxzv.farmerstfc.common.data.DataManagers;
import net.vvxzv.farmerstfc.compat.firmalife.FLEventHandler;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class NeoForgeEventHandler {
    public static void init() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(NeoForgeEventHandler::addReloadListeners);
        bus.addListener(NeoForgeEventHandler::placeCrateBlock);
        bus.addListener(NeoForgeEventHandler::cancelPlaceRottenBlockItem);
        bus.addListener(NeoForgeEventHandler::onFireStart);
        bus.addListener(NeoForgeEventHandler::addFuelToStove);
        bus.addListener(NeoForgeEventHandler::extinguishStove);
        bus.addListener(NeoForgeEventHandler::plantOnRichSoil);

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
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            } else {
                stack.shrink(1);
            }

            event.setCanceled(true);
        }
    }

    public static void cancelPlaceRottenBlockItem(UseItemOnBlockEvent event) {
        Level level = event.getLevel();
        Player player = event.getPlayer();
        if(player == null) return;

        Direction face = event.getFace();
        if (face == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(event.getHand());
        if(stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            BlockPos clickPos = event.getPos();
            BlockState clickedState = level.getBlockState(clickPos);
            BlockPos placePos = clickedState.canBeReplaced() ? clickPos : clickPos.relative(face);
            if (!level.isUnobstructed(state, placePos, CollisionContext.of(player))) {
                return;
            }

            if(stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "cant_place_when_rotten")))) {
                IFood food = FoodCapability.get(stack);
                if (food != null && food.isRotten()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    public static void onFireStart(StartFireEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BlockState state = event.getState();
        if(blockEntity instanceof StoveBlockEntity stove) {
            if(stove.lit(level, pos, state)) {
                event.setCanceled(true);
            }
        }
    }

    public static void addFuelToStove(UseItemOnBlockEvent event) {
        if(!Config.stoveNeedsFuel) {
            return;
        }

        if(event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getLevel();
        BlockPos clickPos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(clickPos);
        if(blockEntity instanceof StoveBlockEntity stove) {
            Player player = event.getPlayer();
            if(player != null) {
                ItemStack stack = player.getMainHandItem();
                if(stack.isEmpty()) {
                    float p = stove.getFuelFillPercentage();
                    player.displayClientMessage(
                            Component.translatable("farmerstfc.stove.fuel")
                                    .append(Component.literal(p*100 + "%").withStyle(ChatFormatting.GRAY)),
                            true
                    );
                }
                if(stove.addFuel(stack)) {
                    if(!player.isCreative()) {
                        stack.shrink(1);
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    public static void extinguishStove(UseItemOnBlockEvent event) {
        Player player = event.getPlayer();
        if(player != null) {
            InteractionHand hand = event.getHand();
            ItemStack heldStack = player.getItemInHand(hand);
            BlockPos pos = event.getPos();
            Level level = event.getLevel();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof StoveBlockEntity stove) {
                if (heldStack.canPerformAction(ItemAbilities.SHOVEL_DIG)) {
                    if(stove.extinguish(level, pos)) {
                        if (!level.isClientSide()) {
                            level.levelEvent(null, 1009, pos, 0);
                        }

                        heldStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                        event.setCanceled(true);
                    }
                }
                if (heldStack.is(Tags.Items.BUCKETS_WATER)) {
                    if (stove.extinguish(level, pos) && !player.getAbilities().instabuild) {
                        if (!level.isClientSide()) {
                            level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }

                        player.setItemInHand(hand, heldStack.getCraftingRemainingItem());
                        event.setCanceled(true);
                    }
                }
                if(heldStack.getItem() instanceof FluidContainerItem) {
                    if (stove.extinguish(level, pos) && !player.getAbilities().instabuild) {
                        if (!level.isClientSide()) {
                            level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }

                        IFluidHandler handler = heldStack.getCapability(Capabilities.FluidHandler.ITEM);
                        if(handler != null) {
                            FluidStack fluidInItem = handler.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                            if(fluidInItem.getFluid().is(FluidTags.WATER)) {
                                player.setItemInHand(hand, new ItemStack(heldStack.getItem()));
                            }
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public static void plantOnRichSoil(BlockEvent.EntityPlaceEvent event) {
        if(!Config.tryBoosting) {
            LevelAccessor level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState belowBlock = level.getBlockState(pos.below());
            if(belowBlock.is(ModBlocks.RICH_SOIL.get())) {
                long halfGlowTick = 0L;

                Block placeBlock = event.getPlacedBlock().getBlock();
                if(placeBlock instanceof TFCSaplingBlock sapling) {
                    halfGlowTick = sapling.getTicksToGrow() / 2;
                } else if(placeBlock instanceof FruitTreeSaplingBlock sapling) {
                    halfGlowTick = sapling.getTicksToGrow() / 2;
                }

                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof TickCounterBlockEntity tickCounterBlockEntity) {
                    long tick = tickCounterBlockEntity.getLastUpdateTick();
                    tickCounterBlockEntity.setLastUpdateTick(tick - halfGlowTick);
                }
            }
        }
    }
}
