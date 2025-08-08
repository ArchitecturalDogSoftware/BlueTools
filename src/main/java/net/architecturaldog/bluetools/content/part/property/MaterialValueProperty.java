package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.MaterialValue;
import org.jetbrains.annotations.NotNull;

public record MaterialValueProperty(@NotNull MaterialValue value) implements PartProperty {

    public static final @NotNull MapCodec<MaterialValueProperty> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance
            .group(MaterialValue.POSITIVE_CODEC.fieldOf("value").forGetter(MaterialValueProperty::value))
            .apply(instance, MaterialValueProperty::new));

    @Override
    public @NotNull PartPropertyType<? extends PartProperty> getType() {
        return BlueToolsPartPropertyTypes.MATERIAL_VALUE.getValue();
    }

}
