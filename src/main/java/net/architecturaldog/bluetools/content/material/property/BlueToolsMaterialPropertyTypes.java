package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.util.Identifier;

public final class BlueToolsMaterialPropertyTypes extends AutoLoader {

    public static final RegistryLoad<MaterialPropertyType<ColorProperty>> COLOR =
        BlueToolsMaterialPropertyTypes.create("color", ColorProperty.CODEC);
    public static final RegistryLoad<MaterialPropertyType<FluidProperty>> FLUID =
        BlueToolsMaterialPropertyTypes.create("fluid", FluidProperty.CODEC);

    private static <P extends MaterialProperty> RegistryLoad<MaterialPropertyType<P>> create(
        final String path,
        final MapCodec<P> codec
    )
    {
        return BlueToolsMaterialPropertyTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends MaterialProperty> RegistryLoad<MaterialPropertyType<P>> create(
        final Identifier identifier,
        final MapCodec<P> codec
    )
    {
        return new RegistryLoad<>(identifier, BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, () -> codec);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("property_types");
    }

}
