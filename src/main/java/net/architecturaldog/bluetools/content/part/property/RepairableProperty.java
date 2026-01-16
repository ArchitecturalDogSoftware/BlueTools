package net.architecturaldog.bluetools.content.part.property;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;

public record RepairableProperty(
    boolean repairable,
    Optional<List<MaterialIngredient>> included,
    Optional<List<MaterialIngredient>> excluded
) implements PartProperty
{

    public static final RepairableProperty DEFAULT = new RepairableProperty(true, Optional.empty(), Optional.empty());

    public static final MapCodec<RepairableProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                Codec.BOOL.optionalFieldOf("repairable", true).forGetter(RepairableProperty::repairable),
                BlueToolsResources.MATERIAL_INGREDIENT
                    .getCodec()
                    .listOf()
                    .optionalFieldOf("include")
                    .forGetter(RepairableProperty::included),
                BlueToolsResources.MATERIAL_INGREDIENT
                    .getCodec()
                    .listOf()
                    .optionalFieldOf("exclude")
                    .forGetter(RepairableProperty::excluded)
            )
            .apply(instance, RepairableProperty::new);
    });

    @Override
    public PartPropertyType<RepairableProperty> getType() {
        return BlueToolsPartPropertyTypes.REPAIRABLE.getValue();
    }

}
