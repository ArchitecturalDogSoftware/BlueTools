package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.dynamic.Codecs;

public record ArmorToughnessProperty(float head, float body, float legs, float feet) implements MaterialProperty {

    public static final ArmorToughnessProperty DEFAULT = new ArmorToughnessProperty(0F, 0F, 0F, 0F);
    public static final MapCodec<ArmorToughnessProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("head").forGetter(ArmorToughnessProperty::head),
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("body").forGetter(ArmorToughnessProperty::body),
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("legs").forGetter(ArmorToughnessProperty::legs),
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("feet").forGetter(ArmorToughnessProperty::feet)
            )
            .apply(instance, ArmorToughnessProperty::new);
    });

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ARMOR_TOUGHNESS.getValue();
    }

}
