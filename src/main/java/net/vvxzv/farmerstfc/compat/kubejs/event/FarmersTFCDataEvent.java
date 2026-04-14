package net.vvxzv.farmerstfc.compat.kubejs.event;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.vvxzv.farmerstfc.common.data.Crate;
import net.vvxzv.farmerstfc.common.data.DecayToRot;
import org.jetbrains.annotations.Nullable;

public class FarmersTFCDataEvent extends KubeJSDataEvent {
    public FarmersTFCDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    public void crate(Crate crate, @Nullable KubeResourceLocation id) {
        this.add(crate, Crate.CODEC, id, "farmerstfc/crate");
    }

    public void crate(Crate crate) {
        this.crate(crate, null);
    }
}
