package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsMaterialTypes extends AutoLoader {

    public static final AutoLoaded<MaterialType<SimpleMaterial>> SIMPLE = BlueToolsMaterialTypes
        .create("simple", SimpleMaterial.CODEC);
    public static final AutoLoaded<MaterialType<UpgradeMaterial>> UPGRADE = BlueToolsMaterialTypes
        .create("upgrade", UpgradeMaterial.CODEC);

    private static <M extends Material> AutoLoaded<MaterialType<M>> create(
        final String path,
        final MapCodec<M> codec
    )
    {
        return BlueToolsMaterialTypes.create(BlueTools.id(path), codec);
    }

    private static <M extends Material> AutoLoaded<MaterialType<M>> create(
        final Identifier identifier,
        final MapCodec<M> codec
    )
    {
        return new AutoLoaded<MaterialType<M>>(identifier, () -> codec).on(CommonLoaded.class, self -> {
            Registry.register(BlueToolsRegistries.MATERIAL_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("material_types");
    }

}
