package net.vvxzv.farmerstfc.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddNbtModifier extends LootModifier {
    private final Item targetItem;
    private final CompoundTag nbt;

    public static final Codec<AddNbtModifier> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions),
                    BuiltInRegistries.ITEM.byNameCodec()
                            .fieldOf("target").forGetter(m -> m.targetItem),
                    CompoundTag.CODEC
                            .fieldOf("nbt").forGetter(m -> m.nbt)
            ).apply(inst, AddNbtModifier::new)
    );

    protected AddNbtModifier(LootItemCondition[] conditionsIn, Item targetItem, CompoundTag nbt) {
        super(conditionsIn);
        this.targetItem = targetItem;
        this.nbt = nbt;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() == targetItem) {
                if (stack.getTag() == null) {
                    stack.setTag(nbt.copy());
                } else {
                    stack.getTag().merge(nbt);
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends net.minecraftforge.common.loot.IGlobalLootModifier> codec() {
        return CODEC;
    }
}
