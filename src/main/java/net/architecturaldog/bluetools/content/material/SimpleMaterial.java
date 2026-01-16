package net.architecturaldog.bluetools.content.material;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;

public record SimpleMaterial(Map<MaterialPropertyType<?>, MaterialProperty> properties) implements Material {

    public static final MapCodec<SimpleMaterial> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(MaterialProperty.CODEC.listOf().fieldOf("properties").forGetter(SimpleMaterial::getProperties))
            .apply(instance, SimpleMaterial::new);
    });

    public SimpleMaterial(final List<MaterialProperty> properties) {
        this(properties.stream().collect(Collectors.toMap(MaterialProperty::getType, Function.identity())));
    }

    @Override
    public MaterialType<SimpleMaterial> getType() {
        return BlueToolsMaterialTypes.SIMPLE.getValue();
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
