package net.vvxzv.farmerstfc.mixin.block;

import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.vvxzv.farmerstfc.common.block.entity.DecayingFoodBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.RiceRollMedleyBlock;

import java.util.function.Supplier;

@Mixin(RiceRollMedleyBlock.class)
public abstract class RiceRollMedleyBlockMixin extends FeastBlock {
    @Final
    @Shadow
    public static IntegerProperty ROLL_SERVINGS;

    public RiceRollMedleyBlockMixin(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof DecayingFoodBlockEntity decaying) {
            if (!Helpers.isBlock(state, newState.getBlock())) {
                if(state.getValue(ROLL_SERVINGS) == 8){
                    Helpers.spawnItem(level, pos, decaying.getStack());
                }
            }
        }

        if (state.hasBlockEntity() && !state.is(newState.getBlock())) {
            level.removeBlockEntity(pos);
        }
    }
}
