package net.architecturaldog.bluetools.content.tool;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface ToolType<T extends Tool> {

    @NotNull MapCodec<T> getCodec();

}
