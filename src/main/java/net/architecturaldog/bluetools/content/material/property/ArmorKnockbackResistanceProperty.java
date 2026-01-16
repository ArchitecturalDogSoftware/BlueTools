package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.dynamic.Codecs;

public record ArmorKnockbackResistanceProperty(
    float head,
    float body,
    float legs,
    float feet
) implements MaterialProperty
{

    public static final ArmorKnockbackResistanceProperty DEFAULT = new ArmorKnockbackResistanceProperty(0F, 0F, 0F, 0F);
    public static final MapCodec<ArmorKnockbackResistanceProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("head").forGetter(ArmorKnockbackResistanceProperty::head),
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("body").forGetter(ArmorKnockbackResistanceProperty::body),
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("legs").forGetter(ArmorKnockbackResistanceProperty::legs),
                Codecs.NON_NEGATIVE_FLOAT.fieldOf("feet").forGetter(ArmorKnockbackResistanceProperty::feet)
            )
            .apply(instance, ArmorKnockbackResistanceProperty::new);
    });

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ARMOR_KNOCKBACK_RESISTANCE.getValue();
    }

}
