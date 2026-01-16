package net.architecturaldog.bluetools.content.resource;

import java.util.List;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.tool.Tool;
import net.minecraft.util.Identifier;

public final class BlueToolsResources extends AutoLoader {

    public static final SimpleJsonResourceManager<MiningLevel> MINING_LEVEL = new SimpleJsonResourceManager<>(
        "mining_level",
        BlueToolsRegistries.Keys.MINING_LEVEL,
        MiningLevel.CODEC.codec()
    );
    public static final SimpleJsonResourceManager<Material> MATERIAL = new SimpleJsonResourceManager<>(
        "material",
        BlueToolsRegistries.Keys.MATERIAL,
        Material.CODEC,
        List.of(BlueToolsResources.MINING_LEVEL.getLoaderId())
    );
    public static final MaterialIngredientResourceManager MATERIAL_INGREDIENT = new MaterialIngredientResourceManager(
        "material_ingredient",
        BlueToolsRegistries.Keys.MATERIAL_INGREDIENT,
        List.of(BlueToolsResources.MATERIAL.getLoaderId())
    );
    public static final SimpleJsonResourceManager<Part> PART = new SimpleJsonResourceManager<>(
        "part",
        BlueToolsRegistries.Keys.PART,
        Part.CODEC,
        List.of(BlueToolsResources.MATERIAL.getLoaderId())
    );
    public static final SimpleJsonResourceManager<Tool> TOOL = new SimpleJsonResourceManager<>(
        "tool",
        BlueToolsRegistries.Keys.TOOL,
        Tool.CODEC,
        List.of(BlueToolsResources.MATERIAL.getLoaderId(), BlueToolsResources.PART.getLoaderId())
    );

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

}
