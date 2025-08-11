package net.architecturaldog.bluetools.content.tool;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.tool.property.DefaultedToolPropertyType;
import net.architecturaldog.bluetools.content.tool.property.ToolProperty;
import net.architecturaldog.bluetools.content.tool.property.ToolPropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface Tool {

    @NotNull Codec<Tool> CODEC = BlueToolsRegistries.TOOL_TYPE.getCodec().dispatch(Tool::getType, ToolType::getCodec);

    @NotNull ToolType<? extends Tool> getType();

    boolean hasProperty(final @NotNull ToolPropertyType<?> type);

    <P extends ToolProperty> @NotNull Optional<P> getProperty(final @NotNull ToolPropertyType<P> type);

    @NotNull List<ToolProperty> getProperties();

    default <P extends ToolProperty> @NotNull P getPropertyOr(
        final @NotNull ToolPropertyType<P> type,
        final @NotNull P property
    )
    {
        return this.getProperty(type).orElse(property);
    }

    default <P extends ToolProperty> @NotNull P getPropertyOrElse(
        final @NotNull ToolPropertyType<P> type,
        final @NotNull Supplier<P> supplier
    )
    {
        return this.getProperty(type).orElseGet(supplier);
    }

    default <P extends ToolProperty> @NotNull P getPropertyOrDefault(final @NotNull DefaultedToolPropertyType<P> type) {
        return this.getPropertyOrElse(type, type::getDefault);
    }

}
