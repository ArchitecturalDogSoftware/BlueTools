package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsMaterialTypes extends AutoLoader {

    public static final @NotNull RegistryLoad<MaterialType<SimpleMaterial>> SIMPLE =
        BlueToolsMaterialTypes.create("simple", SimpleMaterial.CODEC);

    private static <M extends Material> @NotNull RegistryLoad<MaterialType<M>> create(
        final @NotNull String path,
        final @NotNull MapCodec<M> codec
    )
    {
        return BlueToolsMaterialTypes.create(BlueTools.id(path), codec);
    }

    private static <M extends Material> @NotNull RegistryLoad<MaterialType<M>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<M> codec
    )
    {
        return new RegistryLoad<>(identifier, BlueToolsRegistries.MATERIAL_TYPE, () -> codec);
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("material_types");
    }

}
