package net.architecturaldog.bluetools.content.part;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.part.property.PartProperty;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public record SimplePart(@NotNull Map<PartPropertyType<?>, PartProperty> properties) implements Part {

    public static final @NotNull MapCodec<SimplePart> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(PartProperty.CODEC.listOf().fieldOf("properties").forGetter(SimplePart::getProperties))
        .apply(instance, SimplePart::new));

    public SimplePart(final @NotNull List<PartProperty> properties) {
        this(properties.stream().collect(Collectors.toMap(PartProperty::getType, Function.identity())));
    }

    @Override
    public @NotNull PartType<SimplePart> getType() {
        return BlueToolsPartTypes.SIMPLE.getValue();
    }

    @Override
    public boolean hasProperty(final @NotNull PartPropertyType<?> type) {
        return this.properties.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <P extends PartProperty> Optional<P> getProperty(final @NotNull PartPropertyType<P> type) {
        return Optional.ofNullable((P) this.properties.get(type));
    }

    @Override
    public @NotNull List<PartProperty> getProperties() {
        return ImmutableList.copyOf(this.properties.values());
    }

}
