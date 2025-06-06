package net.architecturaldog.bluetools.content.material;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Material {

    public static final MapCodec<Material> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MaterialProperty.CODEC.listOf().fieldOf("properties").forGetter(Material::getProperties)
    ).apply(instance, Material::new));

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

    public static final class Manager extends JsonResourceManager<Material> {

        public Manager() {
            super(
                BlueToolsRegistries.Keys.MATERIAL,
                Material.CODEC.codec(),
                List.of(BlueToolsResources.MATERIAL_MINING_LEVEL.getFabricId())
            );
        }

        @Override
        public String getName() {
            return "Materials";
        }

    }

}
