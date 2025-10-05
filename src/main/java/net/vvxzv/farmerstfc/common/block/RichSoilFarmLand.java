package net.vvxzv.farmerstfc.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class RichSoilFarmLand extends FarmlandBlock {
    public RichSoilFarmLand(ExtendedProperties properties, Supplier<? extends Block> dirt) {
        super(properties, dirt);
    }
}
