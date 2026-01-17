package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
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
        return BlueToolsMaterialTypes.create(BlueToolsHelper.createIdentifier(path), codec);
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
        return BlueToolsHelper.createIdentifier("material_types");
    }

}
