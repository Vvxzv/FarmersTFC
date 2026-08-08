package net.vvxzv.farmerstfc.common.block.entity;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.util.data.Fertilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.Config;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class RichSoilFarmlandBlockEntity extends FarmlandBlockEntity {

    public RichSoilFarmlandBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FARMLAND.get(), pos, state);
    }

    private float getFertilizerTimesValue(){
        return (float) Config.fertilizerOnRichFarmland;
    }

    @Override
    public void addNutrient(FarmlandBlockEntity.@NotNull NutrientType type, float value) {
        this.setNutrient(type, this.getNutrient(type) + value * this.getFertilizerTimesValue());
    }

    @Override
    public void addNutrients(Fertilizer fertilizer, float multiplier) {
        this.addNutrient(NutrientType.NITROGEN, fertilizer.nitrogen() * multiplier);
        this.addNutrient(NutrientType.PHOSPHOROUS, fertilizer.phosphorus() * multiplier);
        this.addNutrient(NutrientType.POTASSIUM, fertilizer.potassium() * multiplier);
        this.markForSync();
    }
}
