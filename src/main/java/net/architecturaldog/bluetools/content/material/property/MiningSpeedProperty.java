package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

public record MiningSpeedProperty(float speed) implements MaterialProperty {

    public static final @NotNull MiningSpeedProperty DEFAULT = new MiningSpeedProperty(1F);
    public static final @NotNull MapCodec<MiningSpeedProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(Codec.FLOAT.fieldOf("speed").forGetter(MiningSpeedProperty::speed))
        .apply(instance, MiningSpeedProperty::new));

    @Override
    public @NotNull MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.MINING_SPEED.getValue();
    }

}
