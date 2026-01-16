package net.architecturaldog.bluetools.content.material;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.DefaultedMaterialPropertyType;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;

public interface Material {

    Codec<Material> CODEC = BlueToolsRegistries.MATERIAL_TYPE
        .getCodec()
        .dispatch(Material::getType, MaterialType::getCodec);

    MaterialType<? extends Material> getType();

    boolean hasProperty(final MaterialPropertyType<?> type);

    <P extends MaterialProperty> Optional<P> getProperty(final MaterialPropertyType<P> type);

    List<MaterialProperty> getProperties();

    default <P extends MaterialProperty> P getPropertyOr(final MaterialPropertyType<P> type, final P property) {
        return this.getProperty(type).orElse(property);
    }

    default <P extends MaterialProperty> P getPropertyOrElse(
        final MaterialPropertyType<P> type,
        final Supplier<P> supplier
    )
    {
        return this.getProperty(type).orElseGet(supplier);
    }

    default <P extends MaterialProperty> P getPropertyOrDefault(final DefaultedMaterialPropertyType<P> type) {
        return this.getPropertyOrElse(type, type::getDefault);
    }

}
