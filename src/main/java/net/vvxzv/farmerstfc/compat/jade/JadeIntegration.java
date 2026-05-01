package net.vvxzv.farmerstfc.compat.jade;

import net.dries007.tfc.common.blocks.TFCCakeBlock;
import net.dries007.tfc.common.blocks.TFCCandleCakeBlock;
import net.vvxzv.farmerstfc.common.block.CrateBlock;
import net.vvxzv.farmerstfc.compat.kubejs.block.DecayingBlockJS;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;

@WailaPlugin
public class JadeIntegration implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration reg) {
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, FeastBlock.class);
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, PieBlock.class);
        reg.registerBlockComponent(DecayingBlockComponentProvider.INSTANCE, DecayingBlockJS.class);

        reg.registerBlockComponent(CakeBlockComponentProvider.INSTANCE, TFCCakeBlock.class);
        reg.registerBlockComponent(CakeBlockComponentProvider.INSTANCE, TFCCandleCakeBlock.class);

        reg.registerBlockComponent(CrateBlockComponentProvider.INSTANCE, CrateBlock.class);

        reg.registerBlockComponent(StoveComponentProvider.INSTANCE, StoveBlock.class);
    }
}
