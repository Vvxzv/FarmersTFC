package net.vvxzv.farmerstfc.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.NeoForgeEventHandler;
import net.vvxzv.farmerstfc.compat.kubejs.block.DecayingBlockJS;
import net.vvxzv.farmerstfc.compat.kubejs.block.entity.DecayingBlockEntityJS;
import net.vvxzv.farmerstfc.compat.kubejs.builder.CrateBlockBuilder;
import net.vvxzv.farmerstfc.compat.kubejs.builder.DecayingBlockBuilder;
import net.vvxzv.farmerstfc.compat.kubejs.event.FarmersTFCDataEvent;
import net.vvxzv.farmerstfc.compat.kubejs.event.FarmersTFCEventHandlers;

public class ModKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, reg -> {
            reg.add(
                    ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "decaying_block"),
                    DecayingBlockBuilder.class,
                    DecayingBlockBuilder::new
            );

            reg.add(
                    ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "crate"),
                    CrateBlockBuilder.class,
                    CrateBlockBuilder::new
            );
        });
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("DecayingBlockEntityJS", DecayingBlockEntityJS.class);
        bindings.add("DecayingBlockJS", DecayingBlockJS.class);
    }

    @Override
    public void registerClasses(ClassFilter filter) {
        filter.deny(FarmersTFCEventHandlers.class);
        filter.deny(FarmersTFC.class.getPackageName() + ".mixin");
        filter.deny(NeoForgeEventHandler.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(FarmersTFCEventHandlers.eventHandler);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        if (FarmersTFCEventHandlers.data.hasListeners()) {
            FarmersTFCEventHandlers.data.post(new FarmersTFCDataEvent(generator));
        }
    }
}
