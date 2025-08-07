package net.architecturaldog.bluetools.content.resource;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.part.Part;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BlueToolsResources extends AutoLoader {

    public static final @NotNull SimpleJsonResourceManager<MiningLevel> MINING_LEVEL = new SimpleJsonResourceManager<>(
        "mining_level",
        BlueToolsRegistries.Keys.MINING_LEVEL,
        MiningLevel.CODEC.codec()
    );

    public static final @NotNull SimpleJsonResourceManager<Material> MATERIAL = new SimpleJsonResourceManager<>(
        "material",
        BlueToolsRegistries.Keys.MATERIAL,
        Material.CODEC,
        List.of(BlueToolsResources.MINING_LEVEL.getFabricId())
    );

    public static final @NotNull SimpleJsonResourceManager<MaterialIngredient> MATERIAL_INGREDIENT =
        new SimpleJsonResourceManager<>(
            "material_ingredient",
            BlueToolsRegistries.Keys.MATERIAL_INGREDIENT,
            MaterialIngredient.CODEC.codec(),
            List.of(BlueToolsResources.MATERIAL.getLoaderId())
        );

    public static final @NotNull SimpleJsonResourceManager<Part> PART =
        new SimpleJsonResourceManager<>("part", BlueToolsRegistries.Keys.PART, Part.CODEC);

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

}
