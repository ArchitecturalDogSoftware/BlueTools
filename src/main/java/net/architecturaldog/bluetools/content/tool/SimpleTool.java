package net.architecturaldog.bluetools.content.tool;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.tool.property.ToolProperty;
import net.architecturaldog.bluetools.content.tool.property.ToolPropertyType;

public record SimpleTool(Map<ToolPropertyType<?>, ToolProperty> properties) implements Tool {

    public static final MapCodec<SimpleTool> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(ToolProperty.CODEC.listOf().fieldOf("properties").forGetter(SimpleTool::getProperties))
            .apply(instance, SimpleTool::new);
    });

    public SimpleTool(final List<ToolProperty> properties) {
        this(properties.stream().collect(Collectors.toMap(ToolProperty::getType, Function.identity())));
    }

    @Override
    public ToolType<? extends Tool> getType() {
        return BlueToolsToolTypes.SIMPLE.getValue();
    }

    @Override
    public boolean hasProperty(final ToolPropertyType<?> type) {
        return this.properties.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends ToolProperty> Optional<P> getProperty(final ToolPropertyType<P> type) {
        return Optional.ofNullable((P) this.properties.get(type));
    }

    @Override
    public List<ToolProperty> getProperties() {
        return ImmutableList.copyOf(this.properties.values());
    }

}
