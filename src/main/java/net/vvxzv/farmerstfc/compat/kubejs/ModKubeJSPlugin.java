package net.vvxzv.farmerstfc.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.dries007.tfc.common.blockentities.DecayingBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockBuilder;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init(){
        RegistryInfo.BLOCK.addType("farmerstfc:decaying_block", DecayingBlockBuilder.class, DecayingBlockBuilder::new);
    }

    @Override
    public void registerBindings(BindingsEvent event){
        event.add("DecayingBlockEntity", DecayingBlockEntity.class);
        event.add("DecayingBlockJS", DecayingBlockBuilder.DecayingBlockJS.class);
    }
}