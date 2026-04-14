package net.vvxzv.farmerstfc.common.data;

import com.google.gson.JsonObject;
import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.ItemDefinition;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.vvxzv.farmerstfc.FarmersTFC;
import net.vvxzv.farmerstfc.common.utils.Utils;
import org.jetbrains.annotations.Nullable;

public class Crate extends ItemDefinition {
    public static final DataManager<Crate> MANAGER = new DataManager<>(
            ResourceLocation.fromNamespaceAndPath(FarmersTFC.MODID, "crate"),
            "crate", Crate::new, Crate::new, Crate::encode, Crate.Packet::new
    );

    private final ResourceLocation block;

    public Crate(ResourceLocation id, JsonObject json) {
        super(id, Ingredient.fromJson(JsonHelpers.get(json, "ingredient")));
        this.block = Utils.getResourceLocation(JsonHelpers.getAsString(json, "block"));
    }

    public Crate(ResourceLocation id, FriendlyByteBuf buffer) {
        super(id, Ingredient.fromNetwork(buffer));
        this.block = buffer.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buffer) {
        this.ingredient.toNetwork(buffer);
        buffer.writeResourceLocation(this.block);
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ResourceLocation getBlockRL() {
        return this.block;
    }

    public static final IndirectHashCollection<Item, Crate> CACHE = IndirectHashCollection.create(
            ItemDefinition::getValidItems,
            MANAGER::getValues
    );

    public static @Nullable Crate get(ItemStack stack) {
        for (Crate crate: CACHE.getAll(stack.getItem())) {
            if(crate.matches(stack)){
                return crate;
            }
        }
        return null;
    }

    public Block getBlock() {
        return BuiltInRegistries.BLOCK.get(this.block);
    }

    public static class Packet extends DataManagerSyncPacket<Crate> {
    }
}
