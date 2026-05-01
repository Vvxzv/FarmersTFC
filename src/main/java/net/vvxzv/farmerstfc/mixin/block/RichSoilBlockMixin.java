package net.vvxzv.farmerstfc.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.registry.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.RichSoilBlock;

@Mixin(RichSoilBlock.class)
public abstract class RichSoilBlockMixin extends Block {
    public RichSoilBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(
            method = "getToolModifiedState",
            at = @At("RETURN"),
            remap = false,
            cancellable = true
    )
    private void getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir() ? Blocks.RICH_SOIL_FARMLAND.get().defaultBlockState() : null);
    }

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/block/RichSoilBlock;tryBoostingPlantsAboveAndBelow(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"), remap = false)
    private void tryBoostingPlantsAboveAndBelow(ServerLevel level, BlockPos pos, RandomSource random) {
        if(Config.tryBoosting) {
            RichSoilBlock.tryBoostingPlantsAboveAndBelow(level, pos, random);
        }
    }
}
