package net.architecturaldog.bluetools.content.tool;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.tool.property.DefaultedToolPropertyType;
import net.architecturaldog.bluetools.content.tool.property.ToolProperty;
import net.architecturaldog.bluetools.content.tool.property.ToolPropertyType;

public interface Tool {

    Codec<Tool> CODEC = BlueToolsRegistries.TOOL_TYPE.getCodec().dispatch(Tool::getType, ToolType::getCodec);

    ToolType<? extends Tool> getType();

    boolean hasProperty(final ToolPropertyType<?> type);

    <P extends ToolProperty> Optional<P> getProperty(final ToolPropertyType<P> type);

    List<ToolProperty> getProperties();

    default <P extends ToolProperty> P getPropertyOr(
        final ToolPropertyType<P> type,
        final P property
    )
    {
        return this.getProperty(type).orElse(property);
    }

    default <P extends ToolProperty> P getPropertyOrElse(
        final ToolPropertyType<P> type,
        final Supplier<P> supplier
    )
    {
        return this.getProperty(type).orElseGet(supplier);
    }

    default <P extends ToolProperty> P getPropertyOrDefault(final DefaultedToolPropertyType<P> type) {
        return this.getPropertyOrElse(type, type::getDefault);
    }

}
