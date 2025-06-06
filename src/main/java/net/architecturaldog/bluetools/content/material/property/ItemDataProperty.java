package net.architecturaldog.bluetools.content.material.property;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.material.MaterialValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public record ItemDataProperty(Map<Entry, MaterialValue> values)
    implements MaterialProperty
{

    public static final MapCodec<ItemDataProperty> CODEC = Codec
        .mapPair(Entry.CODEC.fieldOf("items"), MaterialValue.CODEC.codec().fieldOf("value"))
        .codec()
        .listOf()
        .xmap(
            list -> list.stream().collect(Pair.toMap()),
            map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList()
        )
        .xmap(ItemDataProperty::new, ItemDataProperty::values)
        .fieldOf("values");

    private Stream<Map.Entry<Entry, MaterialValue>> getMapEntries(final BiPredicate<Entry, MaterialValue> predicate) {
        return this.values().entrySet().stream().filter(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    public List<MaterialValue> getValues(final ItemStack stack) {
        return this.getMapEntries((entry, value) -> entry.contains(stack)).map(Map.Entry::getValue).toList();
    }

    public List<Entry> getEntries(final MaterialValue value) {
        return this.getMapEntries((entry, value2) -> value.equals(value2)).map(Map.Entry::getKey).toList();
    }

    @Override
    public MaterialPropertyType<ItemDataProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ITEM_DATA.getValue();
    }

    public record Entry(List<Either<Item, TagKey<Item>>> values) {

        public static final Codec<Entry> CODEC = Codec
            .either(Registries.ITEM.getCodec(), TagKey.codec(RegistryKeys.ITEM))
            .listOf()
            .xmap(Entry::new, Entry::values);

        public boolean contains(final ItemStack stack) {
            return this.values().stream().anyMatch(either -> either.map(stack::isOf, stack::isIn));
        }

    }

}
