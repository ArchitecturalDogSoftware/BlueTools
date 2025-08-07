package net.architecturaldog.bluetools.content.part;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface PartType<P extends Part> {

    @NotNull MapCodec<P> getCodec();

}
