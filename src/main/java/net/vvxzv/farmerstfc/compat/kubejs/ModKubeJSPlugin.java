package net.vvxzv.farmerstfc.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.core.registries.Registries;
import net.vvxzv.farmerstfc.common.blockEntity.DecayingFoodBlockEntity;
import net.vvxzv.farmerstfc.compat.kubejs.blocks.DecayingBlockBuilder;

public class ModKubeJSPlugin implements KubeJSPlugin {
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, reg -> {
            reg.add("farmerstfc:decaying_block", DecayingBlockBuilder.class, DecayingBlockBuilder::new);
        });
    }

    public void registerBindings(BindingRegistry bindings) {
        bindings.add("DecayingFoodBlockEntity", DecayingFoodBlockEntity.class);
        bindings.add("DecayingBlockJS", DecayingBlockBuilder.DecayingBlockJS.class);
    }
}
