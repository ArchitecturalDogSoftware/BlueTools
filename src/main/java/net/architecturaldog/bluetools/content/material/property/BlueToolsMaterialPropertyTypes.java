package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsMaterialPropertyTypes extends AutoLoader {

    public static final @NotNull RegistryLoad<MaterialPropertyType<ColorProperty>> COLOR =
        BlueToolsMaterialPropertyTypes.create("color", ColorProperty.CODEC);
    public static final @NotNull RegistryLoad<MaterialPropertyType<FluidProperty>> FLUID =
        BlueToolsMaterialPropertyTypes.create("fluid", FluidProperty.CODEC);

    private static <P extends MaterialProperty> @NotNull RegistryLoad<MaterialPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec
    )
    {
        return BlueToolsMaterialPropertyTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends MaterialProperty> @NotNull RegistryLoad<MaterialPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec
    )
    {
        return new RegistryLoad<>(identifier, BlueToolsRegistries.MATERIAL_PROPERTY_TYPE, () -> codec);
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("property_types");
    }

}
