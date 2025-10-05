package net.vvxzv.farmerstfc.common.registry;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.farmerstfc.FarmersTFC;

public class item {
    public static final DeferredRegister.Items ITEMS;

    static {
        ITEMS = DeferredRegister.createItems(FarmersTFC.MODID);
    }
}
