package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;

public record WeaponDataProperty(int damage, float speed, float disableShieldSeconds) implements MaterialProperty {

    public static final MapCodec<WeaponDataProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("damage").forGetter(WeaponDataProperty::damage),
        Codec.floatRange(Float.MIN_VALUE, Float.MAX_VALUE).fieldOf("speed").forGetter(WeaponDataProperty::speed),
        Codec
            .floatRange(0.0F, Float.MAX_VALUE)
            .optionalFieldOf("disable_shield_seconds", 0.0F)
            .forGetter(WeaponDataProperty::disableShieldSeconds)
    ).apply(instance, WeaponDataProperty::new));

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.WEAPON_DATA.getValue();
    }

}
