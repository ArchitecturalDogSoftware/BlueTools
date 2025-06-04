package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;

public interface MaterialPropertyType<P extends MaterialProperty> {

    MapCodec<P> getCodec();

    static <P extends MaterialProperty> MaterialPropertyType<P> simple(final MapCodec<P> codec) {
        return new SimpleMaterialPropertyType<>(codec);
    }

}
