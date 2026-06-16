package net.vvxzv.farmerstfc.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddComponentsModifier extends LootModifier {
    private final Item targetItem;
    private final DataComponentPatch componentsToAdd;

    public static final MapCodec<AddComponentsModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions),
                    BuiltInRegistries.ITEM.byNameCodec()
                            .fieldOf("target")
                            .forGetter(m -> m.targetItem),
                    DataComponentPatch.CODEC
                            .fieldOf("components")
                            .forGetter(m -> m.componentsToAdd)
            ).apply(inst, AddComponentsModifier::new)
    );

    protected AddComponentsModifier(LootItemCondition[] conditionsIn,
                                    Item targetItem,
                                    DataComponentPatch componentsToAdd) {
        super(conditionsIn);
        this.targetItem = targetItem;
        this.componentsToAdd = componentsToAdd;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        ObjectArrayList<ItemStack> result = new ObjectArrayList<>();

        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() == targetItem) {
                ItemStack newStack = stack.copy();
                newStack.applyComponents(componentsToAdd);
                result.add(newStack);
            } else {
                result.add(stack);
            }
        }

        return result;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}