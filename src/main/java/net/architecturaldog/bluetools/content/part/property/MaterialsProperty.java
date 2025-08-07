package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public record MaterialsProperty(@NotNull Optional<Set<String>> keys, @NotNull Optional<List<Material>> materials)
    implements PartProperty
{

    public static final @NotNull MaterialsProperty DEFAULT = new MaterialsProperty(
        Optional.of(Set.of("material")),
        Optional.empty()
    );
    public static final @NotNull MapCodec<MaterialsProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            Codecs.NON_EMPTY_STRING
                .listOf()
                .xmap(Set::copyOf, List::copyOf)
                .optionalFieldOf("keys")
                .forGetter(MaterialsProperty::keys),
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
