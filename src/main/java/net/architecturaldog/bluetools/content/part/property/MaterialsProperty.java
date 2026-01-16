package net.architecturaldog.bluetools.content.part.property;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager.Entry;
import net.minecraft.util.StringIdentifiable;

public record MaterialsProperty(
    MaterialsProperty.ListType listType,
    Optional<List<Entry<Material>>> materials
) implements PartProperty
{

    public static final MaterialsProperty DEFAULT = new MaterialsProperty(ListType.EXCLUDE, Optional.empty());

    public static final MapCodec<MaterialsProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                ListType.CODEC.optionalFieldOf("list_type", ListType.INCLUDE).forGetter(MaterialsProperty::listType),
                BlueToolsResources.MATERIAL
                    .getEntryCodec()
                    .listOf()
                    .optionalFieldOf("materials")
                    .forGetter(MaterialsProperty::materials)
            )
            .apply(instance, MaterialsProperty::new);
    });

    public boolean permitsMaterial(final Entry<Material> material) {
        final List<Entry<Material>> entries = this.materials().orElseGet(List::of);

        return this.listType().equals(ListType.INCLUDE) == entries.contains(material);
    }

    public List<Entry<Material>> getPermittedMaterials() {
        final List<Entry<Material>> entries = this.materials().orElseGet(List::of);

        return this.listType.equals(ListType.INCLUDE)
            ? entries
            : BlueToolsResources.MATERIAL.getEntries().stream().filter(entry -> !entries.contains(entry)).toList();
    }

    @Override
    public PartPropertyType<MaterialsProperty> getType() {
        return BlueToolsPartPropertyTypes.MATERIALS.getValue();
    }

    public enum ListType implements StringIdentifiable {

        INCLUDE("include"),
        EXCLUDE("exclude");

        public static final Codec<ListType> CODEC = StringIdentifiable.createCodec(ListType::values);

        private final String key;

        ListType(final String key) {
            this.key = key;
        }

        @Override
        public String asString() {
            return this.key;
        }

    }

}
