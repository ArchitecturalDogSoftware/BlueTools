package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface MaterialType<M extends Material> {

    @NotNull MapCodec<M> getCodec();

}
