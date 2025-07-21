package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.util.Identifier;

public final class BlueToolsMaterialTypes extends AutoLoader {

    public static final RegistryLoaded<MaterialType<SimpleMaterial>> SIMPLE = create("simple", SimpleMaterial.CODEC);

    private static <M extends Material> RegistryLoaded<MaterialType<M>> create(
        final String path,
        final MapCodec<M> codec
    )
    {
        return new RegistryLoaded<>(path, BlueToolsRegistries.MATERIAL_TYPE, () -> codec);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("material_types");
    }

}
