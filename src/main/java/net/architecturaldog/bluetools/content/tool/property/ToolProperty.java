package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import org.jetbrains.annotations.NotNull;

public interface ToolProperty {

    @NotNull Codec<ToolProperty> CODEC = BlueToolsRegistries.TOOL_PROPERTY_TYPE
        .getCodec()
        .dispatch(ToolProperty::getType, ToolPropertyType::getCodec);

    @NotNull ToolPropertyType<? extends ToolProperty> getType();

}
