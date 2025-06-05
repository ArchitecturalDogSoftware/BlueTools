package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.utility.Color;

public record ColorProperty(Color color) implements MaterialProperty {

    public static final MapCodec<ColorProperty> CODEC = Color.RGB_CODEC.xmap(ColorProperty::new, ColorProperty::color);

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.COLOR.getValue();
    }

}
