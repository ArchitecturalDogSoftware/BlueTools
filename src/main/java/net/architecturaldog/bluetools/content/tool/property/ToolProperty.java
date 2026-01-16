package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;

public interface ToolProperty {

    Codec<ToolProperty> CODEC = BlueToolsRegistries.TOOL_PROPERTY_TYPE
        .getCodec()
        .dispatch(ToolProperty::getType, ToolPropertyType::getCodec);

    ToolPropertyType<? extends ToolProperty> getType();

}
