package net.vvxzv.farmerstfc.compat.kubejs.event;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.vvxzv.farmerstfc.common.data.Crate;
import net.vvxzv.farmerstfc.common.data.DecayToRot;
import org.jetbrains.annotations.Nullable;

public class FarmersTFCDataEvent extends KubeJSDataEvent {
    public FarmersTFCDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    @Info("Ingredient ingredient, ResourceLocation block, @Nullable KubeResourceLocation id")
    public void crate(Ingredient ingredient, ResourceLocation block, @Nullable KubeResourceLocation id) {
        this.add(new Crate(ingredient, block), Crate.CODEC, id, "farmerstfc/crate");
    }

    @Info("Ingredient ingredient, ResourceLocation block")
    public void crate(Ingredient ingredient, ResourceLocation block) {
        this.crate(ingredient, block, null);
    }

    @RemapForJS("crateJson")
    @Info("Crate crate, @Nullable KubeResourceLocation id")
    public void crate(Crate crate, @Nullable KubeResourceLocation id) {
        this.add(crate, Crate.CODEC, id, "farmerstfc/crate");
    }

    @RemapForJS("crateJson")
    @Info("Crate crate")
    public void crate(Crate crate) {
        this.crate(crate, null);
    }
}
