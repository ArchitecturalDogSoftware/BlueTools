package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record MaterialsProperty(int count, @NotNull Optional<List<Material>> materials) implements PartProperty {

    public static final @NotNull MaterialsProperty DEFAULT = new MaterialsProperty(1, Optional.empty());
    public static final @NotNull MapCodec<MaterialsProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            Codecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(MaterialsProperty::count),
            BlueToolsResources.MATERIAL
                .getCodec()
                .listOf()
                .optionalFieldOf("materials")
                .forGetter(MaterialsProperty::materials)
        )
        .apply(instance, MaterialsProperty::new));

    @Override
    public @NotNull PartPropertyType<MaterialsProperty> getType() {
        return BlueToolsPartPropertyTypes.MATERIALS.getValue();
    }

}
