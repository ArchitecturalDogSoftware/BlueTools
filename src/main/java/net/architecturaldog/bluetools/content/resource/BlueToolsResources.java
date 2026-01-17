package net.architecturaldog.bluetools.content.resource;

import java.util.List;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.content.BlueToolsRegistryKeys;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.tool.Tool;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.util.Identifier;

public final class BlueToolsResources extends AutoLoader {

    public static final SimpleJsonResourceManager<MiningLevel> MINING_LEVEL = new SimpleJsonResourceManager<>(
        BlueToolsRegistryKeys.MINING_LEVEL,
        MiningLevel.CODEC.codec()
    );
    public static final SimpleJsonResourceManager<Material> MATERIAL = new SimpleJsonResourceManager<>(
        BlueToolsRegistryKeys.MATERIAL,
        Material.CODEC,
        List.of(BlueToolsResources.MINING_LEVEL.getLoaderId())
    );
    public static final MaterialIngredientResourceManager MATERIAL_INGREDIENT = new MaterialIngredientResourceManager(
        BlueToolsRegistryKeys.MATERIAL_INGREDIENT,
        List.of(BlueToolsResources.MATERIAL.getLoaderId())
    );
    public static final SimpleJsonResourceManager<Part> PART = new SimpleJsonResourceManager<>(
        BlueToolsRegistryKeys.PART,
        Part.CODEC,
        List.of(BlueToolsResources.MATERIAL.getLoaderId())
    );
    public static final SimpleJsonResourceManager<Tool> TOOL = new SimpleJsonResourceManager<>(
        BlueToolsRegistryKeys.TOOL,
        Tool.CODEC,
        List.of(BlueToolsResources.MATERIAL.getLoaderId(), BlueToolsResources.PART.getLoaderId())
    );

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("resources");
    }

}
