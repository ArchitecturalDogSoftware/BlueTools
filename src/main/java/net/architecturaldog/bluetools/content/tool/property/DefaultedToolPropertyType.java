package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.MapCodec;

public interface DefaultedToolPropertyType<P extends ToolProperty> extends ToolPropertyType<P> {

    P getDefault();

    static <P extends ToolProperty> DefaultedToolPropertyType<P> of(final MapCodec<P> codec, final P defaultProperty) {
        return new DefaultedToolPropertyType<P>() {

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
