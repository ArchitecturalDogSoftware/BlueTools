package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MiningSpeedProperty(float speed) implements MaterialProperty {

    public static final MiningSpeedProperty DEFAULT = new MiningSpeedProperty(1F);
    public static final MapCodec<MiningSpeedProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(Codec.FLOAT.fieldOf("speed").forGetter(MiningSpeedProperty::speed))
            .apply(instance, MiningSpeedProperty::new);
    });

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.MINING_SPEED.getValue();
    }

}
