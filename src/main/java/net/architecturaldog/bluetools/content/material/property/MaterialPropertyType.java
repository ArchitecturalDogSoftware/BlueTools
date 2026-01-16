package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;

public interface MaterialPropertyType<P extends MaterialProperty> {

    MapCodec<P> getCodec();

}
