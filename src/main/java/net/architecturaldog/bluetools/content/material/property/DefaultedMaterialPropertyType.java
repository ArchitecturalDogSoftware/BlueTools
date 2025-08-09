package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface DefaultedMaterialPropertyType<P extends MaterialProperty> extends MaterialPropertyType<P> {

    @NotNull P getDefault();

    static <P extends MaterialProperty> @NotNull DefaultedMaterialPropertyType<P> of(
        final @NotNull MapCodec<P> codec,
        final @NotNull Supplier<P> defaultProperty
    )
    {
        return new DefaultedMaterialPropertyType<>() {

            @Override
            public @NotNull MapCodec<P> getCodec() {
                return codec;
            }

            @Override
            public @NotNull P getDefault() {
                return defaultProperty.get();
            }

        };
    }

}
