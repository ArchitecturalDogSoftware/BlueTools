package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

public record DurabilityProperty(int tool, int armorMultiplier) implements MaterialProperty {

    public static final @NotNull MapCodec<DurabilityProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            Codecs.POSITIVE_INT.fieldOf("tool").forGetter(DurabilityProperty::tool),
            Codecs.POSITIVE_INT.optionalFieldOf("armor_multiplier", 1).forGetter(DurabilityProperty::armorMultiplier)
        )
        .apply(instance, DurabilityProperty::new));

    @Override
    public @NotNull MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.DURABILITY.getValue();
    }

}
