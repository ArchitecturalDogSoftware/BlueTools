package net.architecturaldog.bluetools.content.material;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public record SimpleMaterial(@NotNull Map<MaterialPropertyType<?>, MaterialProperty> properties) implements Material {

    public static final @NotNull MapCodec<SimpleMaterial> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(MaterialProperty.CODEC.listOf().fieldOf("properties").forGetter(SimpleMaterial::getProperties))
        .apply(instance, SimpleMaterial::new)
    );

    public SimpleMaterial(final @NotNull List<MaterialProperty> properties) {
        this(properties.stream().collect(Collectors.toMap(MaterialProperty::getType, Function.identity())));
    }

    @Override
    public @NotNull MaterialType<SimpleMaterial> getType() {
        return BlueToolsMaterialTypes.SIMPLE.getValue();
    }

    @Override
    public boolean hasProperty(final @NotNull MaterialPropertyType<?> type) {
        return this.properties.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends MaterialProperty> @NotNull Optional<P> getProperty(final @NotNull MaterialPropertyType<P> type) {
        return Optional.ofNullable((P) this.properties.get(type));
    }

    @Override
    public @NotNull List<MaterialProperty> getProperties() {
        return ImmutableList.copyOf(this.properties.values());
    }

}
