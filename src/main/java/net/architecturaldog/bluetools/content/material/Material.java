package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.DefaultedMaterialPropertyType;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface Material {

    @NotNull Codec<Material> CODEC = BlueToolsRegistries.MATERIAL_TYPE
        .getCodec()
        .dispatch(Material::getType, MaterialType::getCodec);

    @NotNull MaterialType<? extends Material> getType();

    boolean hasProperty(final @NotNull MaterialPropertyType<?> type);

    <P extends MaterialProperty> @NotNull Optional<P> getProperty(final @NotNull MaterialPropertyType<P> type);

    @NotNull List<MaterialProperty> getProperties();

    default <P extends MaterialProperty> @NotNull P getPropertyOr(
        final @NotNull MaterialPropertyType<P> type,
        final @NotNull P property
    )
    {
        return this.getProperty(type).orElse(property);
    }

    default <P extends MaterialProperty> @NotNull P getPropertyOrElse(
        final @NotNull MaterialPropertyType<P> type,
        final @NotNull Supplier<P> supplier
    )
    {
        return this.getProperty(type).orElseGet(supplier);
    }

    default <P extends MaterialProperty> @NotNull P getPropertyOrDefault(final @NotNull DefaultedMaterialPropertyType<P> type) {
        return this.getPropertyOrElse(type, type::getDefault);
    }

}
