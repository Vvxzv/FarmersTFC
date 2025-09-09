package net.vvxzv.farmerstfc.mixin;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.vvxzv.farmerstfc.common.registry.block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.RichSoilBlock;

@Mixin(RichSoilBlock.class)
public class RichSoilBlockMixin extends Block {
    public RichSoilBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(
            method = "getToolModifiedState",
            at = @At("RETURN"),
            remap = false,
            cancellable = true
    )
    public void getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir() ? ((Block) block.RICH_SOIL_FARMLAND.get()).defaultBlockState() : null);
    }
}
