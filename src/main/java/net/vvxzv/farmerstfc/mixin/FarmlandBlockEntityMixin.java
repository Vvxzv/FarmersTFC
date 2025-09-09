package net.vvxzv.farmerstfc.mixin;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.vvxzv.farmerstfc.common.registry.block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(FarmlandBlockEntity.class)
public abstract class FarmlandBlockEntityMixin {
    // 处理氮肥
    @ModifyArg(
            method = "addNutrients(Lnet/dries007/tfc/util/Fertilizer;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/common/blockentities/FarmlandBlockEntity;setNutrientWithoutSync(Lnet/dries007/tfc/common/blockentities/FarmlandBlockEntity$NutrientType;F)V",
                    ordinal = 0
            ),
            index = 1,
            remap = false
    )
    private float modifyNitrogenValue(float originalValue) {
        FarmlandBlockEntity self = (FarmlandBlockEntity) (Object) this;
        if (self.getBlockState().getBlock() == block.RICH_SOIL_FARMLAND.get()) {
            // 计算原始肥料贡献值 = 新值 - 旧值
            float oldValue = self.getNutrient(FarmlandBlockEntity.NutrientType.NITROGEN);
            float fertilizerContribution = originalValue - oldValue;

            // 只放大肥料贡献部分，然后加回旧值
            return oldValue + fertilizerContribution * 2.5F;
        }
        return originalValue;
    }

    // 处理磷肥
    @ModifyArg(
            method = "addNutrients(Lnet/dries007/tfc/util/Fertilizer;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/common/blockentities/FarmlandBlockEntity;setNutrientWithoutSync(Lnet/dries007/tfc/common/blockentities/FarmlandBlockEntity$NutrientType;F)V",
                    ordinal = 1
            ),
            index = 1,
            remap = false
    )
    private float modifyPhosphorusValue(float originalValue) {
        FarmlandBlockEntity self = (FarmlandBlockEntity) (Object) this;
        if (self.getBlockState().getBlock() == block.RICH_SOIL_FARMLAND.get()) {
            float oldValue = self.getNutrient(FarmlandBlockEntity.NutrientType.PHOSPHOROUS);
            float fertilizerContribution = originalValue - oldValue;
            return oldValue + fertilizerContribution * 2.5F;
        }
        return originalValue;
    }

    // 处理钾肥
    @ModifyArg(
            method = "addNutrients(Lnet/dries007/tfc/util/Fertilizer;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/common/blockentities/FarmlandBlockEntity;setNutrientWithoutSync(Lnet/dries007/tfc/common/blockentities/FarmlandBlockEntity$NutrientType;F)V",
                    ordinal = 2
            ),
            index = 1,
            remap = false
    )
    private float modifyPotassiumValue(float originalValue) {
        FarmlandBlockEntity self = (FarmlandBlockEntity) (Object) this;
        if (self.getBlockState().getBlock() == block.RICH_SOIL_FARMLAND.get()) {
            float oldValue = self.getNutrient(FarmlandBlockEntity.NutrientType.POTASSIUM);
            float fertilizerContribution = originalValue - oldValue;
            return oldValue + fertilizerContribution * 2.5F;
        }
        return originalValue;
    }
}
