package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record RepairableProperty(
    boolean repairable,
    @NotNull Optional<List<MaterialIngredient>> included,
    @NotNull Optional<List<MaterialIngredient>> excluded
) implements PartProperty
{

    public static final @NotNull RepairableProperty DEFAULT = new RepairableProperty(
        true,
        Optional.empty(),
        Optional.empty()
    );
    public static final @NotNull MapCodec<RepairableProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
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
        .apply(instance, RepairableProperty::new));

    @Override
    public @NotNull PartPropertyType<RepairableProperty> getType() {
        return BlueToolsPartPropertyTypes.REPAIRABLE.getValue();
    }

}
