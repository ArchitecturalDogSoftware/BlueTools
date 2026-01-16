package net.architecturaldog.bluetools.content.part.property;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;

public interface PartProperty {

    Codec<PartProperty> CODEC = BlueToolsRegistries.PART_PROPERTY_TYPE
        .getCodec()
        .dispatch(PartProperty::getType, PartPropertyType::getCodec);

    PartPropertyType<? extends PartProperty> getType();

}
