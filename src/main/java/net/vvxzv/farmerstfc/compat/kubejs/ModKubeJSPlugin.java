package net.vvxzv.farmerstfc.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockBuilder;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init(){
        RegistryInfo.BLOCK.addType("farmerstfc:decaying_block", DecayingBlockBuilder.class, DecayingBlockBuilder::new);
    }
}