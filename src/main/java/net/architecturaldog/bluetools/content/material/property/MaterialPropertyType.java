package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface MaterialPropertyType<P extends MaterialProperty> {

    @NotNull MapCodec<P> getCodec();

}
