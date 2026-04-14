package net.vvxzv.farmerstfc.common.data;

import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.vvxzv.farmerstfc.FarmersTFC;

public class DataManagers {
    public static final ResourceKey<Registry<DataManager<?>>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "data_manager"));

    public static final Registry<DataManager<?>> REGISTRY = new RegistryBuilder<>(KEY).sync(true).create();
    public static final DeferredRegister<DataManager<?>> MANAGERS = DeferredRegister.create(KEY, FarmersTFC.MODID);

    private static void register(DataManager<?> manager) {
        MANAGERS.register(manager.getName(), () -> manager);
    }

    static {
        register(DecayToRot.MANAGER);
        register(Crate.MANAGER);
    }
}
