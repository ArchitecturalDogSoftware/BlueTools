package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.utility.Color;
import org.jetbrains.annotations.NotNull;

public record ColorProperty(@NotNull Color color) implements MaterialProperty {

    public static final @NotNull MapCodec<ColorProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Color.CODEC.fieldOf("color").forGetter(ColorProperty::color)
    ).apply(instance, ColorProperty::new));

    @Override
    public @NotNull MaterialPropertyType<ColorProperty> getType() {
        return BlueToolsMaterialPropertyTypes.COLOR.getValue();
    }

}
