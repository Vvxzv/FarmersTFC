package net.vvxzv.farmerstfc.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.vvxzv.farmerstfc.common.blockEntity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockBuilder;

public class ModKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init(){
        RegistryInfo.BLOCK.addType("farmerstfc:decaying_block", DecayingBlockBuilder.class, DecayingBlockBuilder::new);
    }

    @Override
    public void registerBindings(BindingsEvent event){
        event.add("DecayingFoodBlockEntity", DecayingFoodBlockEntity.class);
        event.add("DecayingBlockJS", DecayingBlockBuilder.DecayingBlockJS.class);
    }
}