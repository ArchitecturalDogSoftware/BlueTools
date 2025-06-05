package net.architecturaldog.bluetools.content.material;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Material {

    public static final MapCodec<Material> CODEC = BlueToolsRegistries.MATERIAL_PROPERTY_TYPE
        .getCodec()
        .<MaterialProperty>dispatch(MaterialProperty::getType, MaterialPropertyType::getCodec)
        .listOf()
        .fieldOf("properties")
        .xmap(Material::new, Material::getProperties);

    private final Map<MaterialPropertyType<?>, MaterialProperty> properties;

    public Material(final List<MaterialProperty> properties) {
        final Map<MaterialPropertyType<?>, MaterialProperty> map = new Object2ObjectOpenHashMap<>(properties.size());

        for (final MaterialProperty property : properties) {
            map.put(property.getType(), property);
        }

        this.properties = map;
    }

    public final List<MaterialProperty> getProperties() {
        return ImmutableList.copyOf(this.properties.values());
    }

    @SuppressWarnings("unchecked")
    public final <P extends MaterialProperty> Optional<P> getProperty(final MaterialPropertyType<P> type) {
        return Optional.ofNullable((P) this.properties.get(type));
    }

}
