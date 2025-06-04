package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;

public record DurabilityProperty(int durability) implements MaterialProperty {

    public static final MapCodec<DurabilityProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("durability").forGetter(DurabilityProperty::durability)
    ).apply(instance, DurabilityProperty::new));

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.DURABILITY.getValue();
    }

}
