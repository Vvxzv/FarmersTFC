package net.vvxzv.farmerstfc.common.block.entity;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.util.Fertilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class RichSoilFarmlandBlockEntity extends FarmlandBlockEntity{

    public RichSoilFarmlandBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FARMLAND.get(), pos, state);
    }

    private static float getFertilizerTimesValue(){
        return (float) Config.fertilizerOnRichFarmland;
    }

    @Override
    public void addNutrients(@NotNull Fertilizer fertilizer, float multiplier) {
        float richMultiplier = multiplier * getFertilizerTimesValue();
        super.addNutrients(fertilizer, richMultiplier);
    }
}
