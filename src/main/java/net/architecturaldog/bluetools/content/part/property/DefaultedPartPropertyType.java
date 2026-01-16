package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;

public interface DefaultedPartPropertyType<P extends PartProperty> extends PartPropertyType<P> {

    P getDefault();

    static <P extends PartProperty> DefaultedPartPropertyType<P> of(final MapCodec<P> codec, final P defaultProperty) {
        return new DefaultedPartPropertyType<>() {

            @Override
            public MapCodec<P> getCodec() {
                return codec;
            }

            @Override
            public P getDefault() {
                return defaultProperty;
            }

        };
    }

}
