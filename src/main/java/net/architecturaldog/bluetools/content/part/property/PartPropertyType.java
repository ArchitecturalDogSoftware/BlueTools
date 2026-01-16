package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;

public interface PartPropertyType<P extends PartProperty> {

    MapCodec<P> getCodec();

}
