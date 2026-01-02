package net.vvxzv.farmerstfc.mixin;

import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

@Mixin(StoveBlockEntity.class)
public class StoveBlockEntityMixin extends SyncedBlockEntity {
    private static float stoveTemperature(){
        return (float) Config.stoveTemperature;
    }

    public StoveBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(method = "cookingTick", at = @At("HEAD"), remap = false)
    private static void cookingTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove, CallbackInfo ci){
        boolean isStoveLit = state.getValue(StoveBlock.LIT);
        if(isStoveLit){
            BlockEntity above = level.getBlockEntity(pos.above());
            if (above != null) {
                above.getCapability(HeatCapability.BLOCK_CAPABILITY).ifPresent((cap) -> {
                    float blockTemperature = cap.getTemperature();
                    if(blockTemperature < stoveTemperature()){
                        float setTemperature = blockTemperature + 2;
                        if(setTemperature > stoveTemperature()) setTemperature = stoveTemperature();
                        cap.setTemperatureIfWarmer(setTemperature);
                    }
                });
            }
        }
    }
}
