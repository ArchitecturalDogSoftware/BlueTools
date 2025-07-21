package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.utility.Color;

public record ColorProperty(Color color) implements MaterialProperty {

    public static final MapCodec<ColorProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Color.CODEC.fieldOf("color").forGetter(ColorProperty::color)
    ).apply(instance, ColorProperty::new));

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.COLOR.getValue();
    }

}
