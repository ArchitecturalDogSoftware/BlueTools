package net.architecturaldog.bluetools.content.part;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.part.property.PartProperty;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;

public record SimplePart(Map<PartPropertyType<?>, PartProperty> properties) implements Part {

    public static final MapCodec<SimplePart> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(PartProperty.CODEC.listOf().fieldOf("properties").forGetter(SimplePart::getProperties))
            .apply(instance, SimplePart::new);
    });

    public SimplePart(final List<PartProperty> properties) {
        this(properties.stream().collect(Collectors.toMap(PartProperty::getType, Function.identity())));
    }

    @Override
    public PartType<SimplePart> getType() {
        return BlueToolsPartTypes.SIMPLE.getValue();
    }

    @Override
    public boolean hasProperty(final PartPropertyType<?> type) {
        return this.properties.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends PartProperty> Optional<P> getProperty(final PartPropertyType<P> type) {
        return Optional.ofNullable((P) this.properties.get(type));
    }

    @Override
    public List<PartProperty> getProperties() {
        return ImmutableList.copyOf(this.properties.values());
    }

}
