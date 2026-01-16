package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;

public interface MaterialProperty {

    Codec<MaterialProperty> CODEC = BlueToolsRegistries.MATERIAL_PROPERTY_TYPE
        .getCodec()
        .dispatch(MaterialProperty::getType, MaterialPropertyType::getCodec);

    MaterialPropertyType<? extends MaterialProperty> getType();

}
