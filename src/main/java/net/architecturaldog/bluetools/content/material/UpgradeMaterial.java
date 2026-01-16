package net.architecturaldog.bluetools.content.material;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.minecraft.registry.RegistryKey;

public record UpgradeMaterial(
    RegistryKey<Material> parent,
    Map<MaterialPropertyType<?>, MaterialProperty> properties
) implements Material
{

    public static final MapCodec<UpgradeMaterial> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                RegistryKey
                    .createCodec(BlueToolsRegistries.Keys.MATERIAL)
                    .fieldOf("parent")
                    .forGetter(UpgradeMaterial::parent),
                MaterialProperty.CODEC.listOf().fieldOf("properties").forGetter(UpgradeMaterial::getProperties)
            )
            .apply(instance, UpgradeMaterial::new);
    });

    public UpgradeMaterial(final RegistryKey<Material> parent, final List<MaterialProperty> properties) {
        this(parent, properties.stream().collect(Collectors.toMap(MaterialProperty::getType, Function.identity())));
    }

    public JsonResourceManager.Entry<Material> getParent() {
        return BlueToolsResources.MATERIAL.getEntry(this.parent).orElseThrow();
    }

    @Override
    public MaterialType<UpgradeMaterial> getType() {
        return BlueToolsMaterialTypes.UPGRADE.getValue();
    }

    @Override
    public boolean hasProperty(final MaterialPropertyType<?> type) {
        return this.properties.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends MaterialProperty> Optional<P> getProperty(final MaterialPropertyType<P> type) {
        return Optional.ofNullable((P) this.properties.get(type));
    }

    @Override
    public List<MaterialProperty> getProperties() {
        return ImmutableList.copyOf(this.properties.values());
    }

}
