package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface Material {

    @NotNull Codec<Material> CODEC = BlueToolsRegistries.MATERIAL_TYPE
        .getCodec()
        .dispatch(Material::getType, MaterialType::getCodec);

    @NotNull MaterialType<? extends Material> getType();

    boolean hasProperty(final @NotNull MaterialPropertyType<?> type);

    <P extends MaterialProperty> @NotNull Optional<P> getProperty(final @NotNull MaterialPropertyType<P> type);

    @NotNull List<MaterialProperty> getProperties();

}
