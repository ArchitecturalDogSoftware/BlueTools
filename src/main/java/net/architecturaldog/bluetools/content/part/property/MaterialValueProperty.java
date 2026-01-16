package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.material.MaterialValue;

public record MaterialValueProperty(MaterialValue value) implements PartProperty {

    public static final MaterialValueProperty DEFAULT = new MaterialValueProperty(new MaterialValue.Ingots(1L));

    public static final MapCodec<MaterialValueProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(MaterialValue.POSITIVE_CODEC.fieldOf("value").forGetter(MaterialValueProperty::value))
            .apply(instance, MaterialValueProperty::new);
    });

    @Override
    public PartPropertyType<? extends PartProperty> getType() {
        return BlueToolsPartPropertyTypes.MATERIAL_VALUE.getValue();
    }

}
