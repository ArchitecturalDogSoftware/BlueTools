package net.architecturaldog.bluetools.content.material.property;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.material.MaterialValue;
import net.minecraft.recipe.Ingredient;

import java.util.Map;

public record ItemDataProperty(Map<Ingredient, MaterialValue> values)
    implements MaterialProperty
{

    public static final MapCodec<ItemDataProperty> CODEC = Codec
        .mapPair(Ingredient.CODEC.fieldOf("ingredient"), MaterialValue.CODEC.fieldOf("value"))
        .codec()
        .listOf()
        .xmap(
            list -> list.stream().collect(Pair.toMap()),
            map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList()
        )
        .xmap(ItemDataProperty::new, ItemDataProperty::values)
        .fieldOf("values");

    @Override
    public MaterialPropertyType<ItemDataProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ITEM_DATA.getValue();
    }

}
