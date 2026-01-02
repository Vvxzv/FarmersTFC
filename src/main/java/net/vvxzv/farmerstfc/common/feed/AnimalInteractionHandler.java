package net.vvxzv.farmerstfc.common.feed;

import net.dries007.tfc.common.entities.livestock.horse.TFCHorse;
import net.dries007.tfc.common.entities.livestock.pet.Dog;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.vvxzv.farmerstfc.FarmersTFC;
import vectorwing.farmersdelight.common.item.DogFoodItem;
import vectorwing.farmersdelight.common.item.HorseFeedItem;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.MathUtils;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = FarmersTFC.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AnimalInteractionHandler {
    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);

        if (!(entity instanceof LivingEntity target)) {
            return; // 若目标不是生物实体，直接返回，不干扰默认交互
        }

        // 1. 处理狗粮给狗添加效果
        if (stack.getItem() instanceof DogFoodItem && target instanceof Dog dog) {
            handleDogFoodInteraction(player, dog, stack);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true); // 取消默认交互，避免重复处理
        }

        // 2. 处理马饲料给马添加效果
        if (stack.getItem() instanceof HorseFeedItem && target instanceof TFCHorse horse) {
            handleHorseFeedInteraction(player, horse, stack);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static void handleDogFoodInteraction(Player player, Dog dog, ItemStack stack) {
        if (!dog.isAlive()) return;

        // 恢复生命值
        dog.setHealth(dog.getMaxHealth());

        // 添加效果（这里示例添加速度和力量效果，可根据需要修改）
        dog.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0));
        dog.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 0));
        dog.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0));

        // 播放音效
        dog.level().playSound(null, dog.blockPosition(),
                net.minecraft.sounds.SoundEvents.GENERIC_EAT,
                net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 0.8F);

        // 生成粒子效果
        spawnHearts(dog);

        // 消耗物品（创造模式不消耗）
        if (!player.isCreative()) {
            stack.shrink(1);
        }
    }

    private static void handleHorseFeedInteraction(Player player, TFCHorse horse, ItemStack stack) {
        if (!horse.isAlive()) return;

        // 恢复生命值
        horse.setHealth(horse.getMaxHealth());

        // 添加效果（示例：抗性提升和速度）
        horse.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 1));
        horse.addEffect(new MobEffectInstance(MobEffects.JUMP, 6000, 0));

        // 播放音效
        horse.level().playSound(null, horse.blockPosition(),
                net.minecraft.sounds.SoundEvents.HORSE_EAT,
                net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 0.9F);

        // 生成粒子效果
        spawnHearts(horse);

        // 消耗物品
        if (!player.isCreative()) {
            stack.shrink(1);
        }
    }

    private static void spawnHearts(LivingEntity entity) {
        for(int i = 0; i < 5; ++i) {
            double xSpeed = MathUtils.RAND.nextGaussian() * 0.02;
            double ySpeed = MathUtils.RAND.nextGaussian() * 0.02;
            double zSpeed = MathUtils.RAND.nextGaussian() * 0.02;
            entity.level().addParticle(ModParticleTypes.STAR.get(), entity.getRandomX(1.0F), entity.getRandomY() + 0.5F, entity.getRandomZ(1.0F), xSpeed, ySpeed, zSpeed);
        }
    }
}
