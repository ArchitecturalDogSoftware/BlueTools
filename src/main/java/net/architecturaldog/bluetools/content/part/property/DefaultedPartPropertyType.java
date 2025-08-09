package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface DefaultedPartPropertyType<P extends PartProperty> extends PartPropertyType<P> {

    @NotNull P getDefault();

    static <P extends PartProperty> @NotNull DefaultedPartPropertyType<P> of(
        final @NotNull MapCodec<P> codec,
        final @NotNull P defaultProperty
    )
    {
        return new DefaultedPartPropertyType<>() {

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
