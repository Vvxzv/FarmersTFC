package net.vvxzv.farmerstfc.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.vvxzv.farmerstfc.compat.kubejs.block.DecayingBlockJS;
import net.vvxzv.farmerstfc.compat.kubejs.block.entity.DecayingBlockEntityJS;
import net.vvxzv.farmerstfc.compat.kubejs.builder.CrateBlockBuilder;
import net.vvxzv.farmerstfc.compat.kubejs.builder.DecayingBlockBuilder;

public class ModKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init(){
        RegistryInfo.BLOCK.addType("farmerstfc:decaying_block", DecayingBlockBuilder.class, DecayingBlockBuilder::new);
        RegistryInfo.BLOCK.addType("farmerstfc:crate", CrateBlockBuilder.class, CrateBlockBuilder::new);
    }

    @Override
    public void registerBindings(BindingsEvent event){
        event.add("DecayingBlockEntityJS", DecayingBlockEntityJS.class);
        event.add("DecayingBlockJS", DecayingBlockJS.class);
    }
}