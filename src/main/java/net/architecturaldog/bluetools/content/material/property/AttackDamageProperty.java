package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

public record AttackDamageProperty(float damage) implements MaterialProperty {

    public static final @NotNull AttackDamageProperty DEFAULT = new AttackDamageProperty(0F);
    public static final @NotNull MapCodec<AttackDamageProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(Codec.FLOAT.fieldOf("damage").forGetter(AttackDamageProperty::damage))
        .apply(instance, AttackDamageProperty::new));

    @Override
    public @NotNull MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.ATTACK_DAMAGE.getValue();
    }

}
