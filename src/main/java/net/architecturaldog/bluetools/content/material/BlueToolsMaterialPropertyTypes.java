package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.property.ColorProperty;
import net.architecturaldog.bluetools.content.material.property.FluidProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialProperty;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.util.Identifier;

public final class BlueToolsMaterialPropertyTypes extends AutoLoader {

    public static final RegistryLoad<MaterialPropertyType<ColorProperty>> COLOR =
        create("color", ColorProperty.CODEC);
    public static final RegistryLoad<MaterialPropertyType<FluidProperty>> FLUID =
        create("fluid", FluidProperty.CODEC);

    private static <P extends MaterialProperty> RegistryLoad<MaterialPropertyType<P>> create(
        final String path,
        final MapCodec<P> codec
    )
    {
        return new RegistryLoad<>(path, BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, () -> codec);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("property_types");
    }

}
