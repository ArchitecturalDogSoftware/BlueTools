package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface PartPropertyType<P extends PartProperty> {

    @NotNull MapCodec<P> getCodec();

}
