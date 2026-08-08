package net.vvxzv.farmerstfc.mixin.block.entity;

import com.eerussianguy.firmalife.common.blockentities.FLBeehiveBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.IFarmland;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FLBeehiveBlockEntity.class)
public class FLBeehiveBlockEntityMixin {

    @Final
    @Shadow(remap = false)
    private static FarmlandBlockEntity.NutrientType N;

    @Final
    @Shadow(remap = false)
    private static FarmlandBlockEntity.NutrientType P;

    @Final
    @Shadow(remap = false)
    private static FarmlandBlockEntity.NutrientType K;

    @Inject(method = "receiveNutrients", at = @At("HEAD"), cancellable = true, remap = false)
    private void receiveNutrients(IFarmland farmland, float cap, float nitrogen, float phosphorous, float potassium, CallbackInfo ci) {
        this.addNutrient(farmland, N, cap, nitrogen);
        this.addNutrient(farmland, P, cap, phosphorous);
        this.addNutrient(farmland, K, cap, potassium);
        ci.cancel();
    }

    @Unique
    private void addNutrient(IFarmland farmland, FarmlandBlockEntity.NutrientType type, float cap, float amount) {
        float current = farmland.getNutrient(type);
        if (current < cap) {
            if (current + amount > cap) {
                farmland.setNutrient(type, cap);
            } else {
                farmland.addNutrient(type, amount);
            }
        }
    }
}
