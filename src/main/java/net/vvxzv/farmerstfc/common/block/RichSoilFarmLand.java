package net.vvxzv.farmerstfc.common.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.farmerstfc.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RichSoilFarmLand extends FarmlandBlock {
    public RichSoilFarmLand(ExtendedProperties properties, Supplier<? extends Block> dirt) {
        super(properties, dirt);
    }

    @Override
    public void addHoeOverlayInfo(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Consumer<Component> text, boolean isDebug) {
        level.getBlockEntity(pos, BlockEntities.FARMLAND.get()).ifPresent((farmland) -> farmland.addHoeOverlayInfo(level, pos, text, true, true));
    }
}
