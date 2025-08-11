package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface ToolPropertyType<P extends ToolProperty> {

    @NotNull MapCodec<P> getCodec();

}
