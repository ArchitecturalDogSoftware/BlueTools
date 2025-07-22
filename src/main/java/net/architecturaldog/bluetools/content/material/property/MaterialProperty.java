package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import org.jetbrains.annotations.NotNull;

public interface MaterialProperty {

    @NotNull Codec<MaterialProperty> CODEC = BlueToolsRegistries.MATERIAL_PROPERTY_TYPE
        .getCodec()
        .dispatch(MaterialProperty::getType, MaterialPropertyType::getCodec);

    @NotNull MaterialPropertyType<? extends MaterialProperty> getType();

}
