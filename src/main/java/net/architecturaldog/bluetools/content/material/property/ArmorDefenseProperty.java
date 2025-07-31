package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

public record ArmorDefenseProperty(int head, int body, int legs, int feet) implements MaterialProperty {

    public static final @NotNull MapCodec<ArmorDefenseProperty> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.NON_NEGATIVE_INT.fieldOf("head").forGetter(ArmorDefenseProperty::head),
            Codecs.NON_NEGATIVE_INT.fieldOf("body").forGetter(ArmorDefenseProperty::body),
            Codecs.NON_NEGATIVE_INT.fieldOf("legs").forGetter(ArmorDefenseProperty::legs),
            Codecs.NON_NEGATIVE_INT.fieldOf("feet").forGetter(ArmorDefenseProperty::feet)
        ).apply(instance, ArmorDefenseProperty::new));

    @Override
    public @NotNull MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ARMOR_DEFENSE.getValue();
    }

}
