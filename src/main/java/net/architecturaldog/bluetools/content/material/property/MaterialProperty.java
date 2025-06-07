package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;

public interface MaterialProperty {

    Codec<MaterialProperty> CODEC = BlueToolsRegistries.MATERIAL_PROPERTY_TYPE
        .getCodec()
        .dispatch(MaterialProperty::getType, MaterialPropertyType::getCodec);

    MaterialPropertyType<? extends MaterialProperty> getType();

    default MapCodec<? extends MaterialProperty> getCodec() {
        return this.getType().getCodec();
    }

}
