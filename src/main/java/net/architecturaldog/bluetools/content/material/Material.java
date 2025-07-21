package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;

import java.util.List;
import java.util.Optional;

public interface Material {

    Codec<Material> CODEC = BlueToolsRegistries.MATERIAL_TYPE
        .getCodec()
        .dispatch(Material::getType, MaterialType::getCodec);

    MaterialType<? extends Material> getType();

    boolean hasProperty(final MaterialPropertyType<?> type);

    <P extends MaterialProperty> Optional<P> getProperty(final MaterialPropertyType<P> type);

    List<MaterialProperty> getProperties();

}
