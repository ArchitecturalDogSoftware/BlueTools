package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record MaterialsProperty(
    @NotNull MaterialsProperty.ListType listType,
    @NotNull Optional<List<JsonResourceManager.Entry<Material>>> materials
)
    implements PartProperty
{

    public static final @NotNull MaterialsProperty DEFAULT = new MaterialsProperty(ListType.EXCLUDE, Optional.empty());
    public static final @NotNull MapCodec<MaterialsProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            ListType.CODEC.optionalFieldOf("list_type", ListType.INCLUDE).forGetter(MaterialsProperty::listType),
            BlueToolsResources.MATERIAL
                .getEntryCodec()
                .listOf()
                .optionalFieldOf("materials")
                .forGetter(MaterialsProperty::materials)
        ).apply(instance, MaterialsProperty::new));

    public @NotNull List<JsonResourceManager.Entry<Material>> getPermittedMaterials() {
        final @NotNull List<JsonResourceManager.Entry<Material>> entries = this.materials().orElseGet(List::of);

        return this.listType.equals(ListType.INCLUDE)
            ? entries
            : BlueToolsResources.MATERIAL.getEntries().stream().filter(entry -> !entries.contains(entry)).toList();
    }

    @Override
    public @NotNull PartPropertyType<MaterialsProperty> getType() {
        return BlueToolsPartPropertyTypes.MATERIALS.getValue();
    }

    public enum ListType implements StringIdentifiable {

        INCLUDE("include"),
        EXCLUDE("exclude");

        public static final @NotNull Codec<ListType> CODEC = StringIdentifiable.createCodec(ListType::values);

        private final @NotNull String key;

        ListType(final @NotNull String key) {
            this.key = key;
        }

        @Override
        public String asString() {
            return this.key;
        }
    }

}
