package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface DefaultedToolPropertyType<P extends ToolProperty> extends ToolPropertyType<P> {

    @NotNull P getDefault();

    static <P extends ToolProperty> @NotNull DefaultedToolPropertyType<P> of(
        final @NotNull MapCodec<P> codec,
        final @NotNull P defaultProperty
    )
    {
        return new DefaultedToolPropertyType<P>() {

            @Override
            public @NotNull MapCodec<P> getCodec() {
                return codec;
            }

            @Override
            public @NotNull P getDefault() {
                return defaultProperty;
            }

        };
    }

}
