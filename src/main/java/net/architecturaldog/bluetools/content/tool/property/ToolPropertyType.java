package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.MapCodec;

public interface ToolPropertyType<P extends ToolProperty> {

    MapCodec<P> getCodec();

}
