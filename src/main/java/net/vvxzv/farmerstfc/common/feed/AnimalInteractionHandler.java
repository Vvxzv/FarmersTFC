package net.vvxzv.farmerstfc.common.feed;

import net.dries007.tfc.common.entities.livestock.horse.TFCHorse;
import net.dries007.tfc.common.entities.livestock.pet.Dog;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vvxzv.farmerstfc.FarmersTFC;
import vectorwing.farmersdelight.common.item.DogFoodItem;
import vectorwing.farmersdelight.common.item.HorseFeedItem;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.MathUtils;

@Mod.EventBusSubscriber(modid = FarmersTFC.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnimalInteractionHandler {

    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);

        if (!(entity instanceof LivingEntity target)) {
            return;
        }

        if (stack.getItem() instanceof DogFoodItem && target instanceof Dog dog) {
            handleDogFoodInteraction(player, dog, stack);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }

        if (stack.getItem() instanceof HorseFeedItem && target instanceof TFCHorse horse) {
            handleHorseFeedInteraction(player, horse, stack);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static void handleDogFoodInteraction(Player player, Dog dog, ItemStack stack) {
        if (!dog.isAlive()) return;

        dog.setHealth(dog.getMaxHealth());

        dog.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0));
        dog.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 0));
        dog.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0));

        dog.level().playSound(
                null,
                dog.blockPosition(),
                net.minecraft.sounds.SoundEvents.GENERIC_EAT,
                net.minecraft.sounds.SoundSource.PLAYERS,
                0.8F,
                0.8F
        );

        spawnHearts(dog);

        if (!player.isCreative()) {
            stack.shrink(1);
        }
    }

    private static void handleHorseFeedInteraction(Player player, TFCHorse horse, ItemStack stack) {
        if (!horse.isAlive()) return;

        horse.setHealth(horse.getMaxHealth());

        horse.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 1));
        horse.addEffect(new MobEffectInstance(MobEffects.JUMP, 6000, 0));

        horse.level().playSound(
                null,
                horse.blockPosition(),
                net.minecraft.sounds.SoundEvents.HORSE_EAT,
                net.minecraft.sounds.SoundSource.PLAYERS,
                0.8F,
                0.9F
        );

        spawnHearts(horse);

        if (!player.isCreative()) {
            stack.shrink(1);
        }
    }

    private static void spawnHearts(LivingEntity entity) {
        for(int i = 0; i < 5; ++i) {
            double xSpeed = MathUtils.RAND.nextGaussian() * 0.02;
            double ySpeed = MathUtils.RAND.nextGaussian() * 0.02;
            double zSpeed = MathUtils.RAND.nextGaussian() * 0.02;
            entity.level().addParticle(ModParticleTypes.STAR.get(), entity.getRandomX(1.0F), entity.getRandomY() + (double)0.5F, entity.getRandomZ(1.0F), xSpeed, ySpeed, zSpeed);
        }
    }
}
