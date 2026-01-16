package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AttackDamageProperty(float damage) implements MaterialProperty {

    public static final AttackDamageProperty DEFAULT = new AttackDamageProperty(0F);
    public static final MapCodec<AttackDamageProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(Codec.FLOAT.fieldOf("damage").forGetter(AttackDamageProperty::damage))
            .apply(instance, AttackDamageProperty::new);
    });

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ATTACK_DAMAGE.getValue();
    }

}
