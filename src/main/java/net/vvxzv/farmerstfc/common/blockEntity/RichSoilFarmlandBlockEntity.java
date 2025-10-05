package net.vvxzv.farmerstfc.common.blockEntity;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.util.data.Fertilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.registry.blockEntities;


public class RichSoilFarmlandBlockEntity extends FarmlandBlockEntity{

    public RichSoilFarmlandBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)blockEntities.FARMLAND.get(), pos, state);
    }

    protected RichSoilFarmlandBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private static float getFertilizerTimesValue(){
        return (float) Config.fertilizerOnRichFarmland;
    }

    @Override
    public void addNutrients(Fertilizer fertilizer, float multiplier) {
        float richMultiplier = multiplier * getFertilizerTimesValue();
        super.addNutrients(fertilizer, richMultiplier);
    }
}
