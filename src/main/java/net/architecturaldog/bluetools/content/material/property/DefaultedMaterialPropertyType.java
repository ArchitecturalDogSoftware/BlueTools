package net.architecturaldog.bluetools.content.material.property;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

public interface DefaultedMaterialPropertyType<P extends MaterialProperty> extends MaterialPropertyType<P> {

    P getDefault();

    static <P extends MaterialProperty> DefaultedMaterialPropertyType<P> of(
        final MapCodec<P> codec,
        final Supplier<P> defaultProperty
    )
    {
        return new DefaultedMaterialPropertyType<>() {

            @Override
            public MapCodec<P> getCodec() {
                return codec;
            }

            @Override
            public P getDefault() {
                return defaultProperty.get();
            }

        };
    }

}
