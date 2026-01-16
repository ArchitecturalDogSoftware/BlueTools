package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AttackSpeedProperty(float speed) implements MaterialProperty {

    public static final AttackSpeedProperty DEFAULT = new AttackSpeedProperty(1.6F);
    public static final MapCodec<AttackSpeedProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(Codec.FLOAT.fieldOf("speed").forGetter(AttackSpeedProperty::speed))
            .apply(instance, AttackSpeedProperty::new);
    });

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ATTACK_SPEED.getValue();
    }

}
