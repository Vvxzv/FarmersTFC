package net.vvxzv.farmerstfc.common.data;

import com.google.gson.JsonObject;
import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.Supplier;

public final class DecayToRot {
    public static final DataManager<DecayToRot> MANAGER = new DataManager<>(
            ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "decay_to_rot"),
            "decay_to_rot", DecayToRot::new, DecayToRot::new, DecayToRot::encode, DecayToRot.Packet::new
    );

    private final ResourceLocation id;
    private final ResourceLocation decayingBlock;
    private final ResourceLocation rottenBlock;

    public DecayToRot(ResourceLocation id, JsonObject json) {
        this.id = id;
        this.decayingBlock = Utils.getResourceLocation(JsonHelpers.getAsString(json, "decayingBlock"));
        this.rottenBlock = Utils.getResourceLocation(JsonHelpers.getAsString(json, "rottenBlock"));
    }

    public DecayToRot(ResourceLocation id, FriendlyByteBuf buffer) {
        this.id = id;
        this.decayingBlock = buffer.readResourceLocation();
        this.rottenBlock = buffer.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.decayingBlock);
        buffer.writeResourceLocation(this.rottenBlock);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ResourceLocation getDecayingBlock() {
        return this.decayingBlock;
    }

    public ResourceLocation getRottenBlock() {
        return this.rottenBlock;
    }

    public static final IndirectHashCollection<ResourceLocation, DecayToRot> CACHE = IndirectHashCollection.create(
            c -> Collections.singleton(c.decayingBlock),
            MANAGER::getValues
    );

    public static @Nullable Supplier<Block> getRotten(ResourceLocation decay) {
        for (DecayToRot decayToRot: CACHE.getAll(decay)) {
            if(decayToRot.decayingBlock.equals(decay)){
                return () -> BuiltInRegistries.BLOCK.get(decayToRot.rottenBlock);
            }
        }

        return null;
    }

    public static @Nullable Supplier<Block> getRotten(Block decay) {
        return getRotten(BuiltInRegistries.BLOCK.getKey(decay));
    }

    public static @Nullable ResourceLocation getRottenRL(ResourceLocation decay) {
        for (DecayToRot decayToRot: CACHE.getAll(decay)) {
            if(decayToRot.decayingBlock.equals(decay)){
                return decayToRot.rottenBlock;
            }
        }

        return null;
    }

    public static class Packet extends DataManagerSyncPacket<DecayToRot> {
    }
}
