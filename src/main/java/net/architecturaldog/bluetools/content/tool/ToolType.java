package net.architecturaldog.bluetools.content.tool;

import com.mojang.serialization.MapCodec;

public interface ToolType<T extends Tool> {

    MapCodec<T> getCodec();

}
