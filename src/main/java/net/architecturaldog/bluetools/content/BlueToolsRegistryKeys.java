package net.architecturaldog.bluetools.content;

import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialIngredient;
import net.architecturaldog.bluetools.content.material.MaterialType;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.part.PartType;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;
import net.architecturaldog.bluetools.content.tool.Tool;
import net.architecturaldog.bluetools.content.tool.ToolType;
import net.architecturaldog.bluetools.content.tool.property.ToolPropertyType;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class BlueToolsRegistryKeys {

    public static final RegistryKey<Registry<MaterialIngredient>> MATERIAL_INGREDIENT = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("material_ingredient"));
    public static final RegistryKey<Registry<MaterialPropertyType<?>>> MATERIAL_PROPERTY_TYPE = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("material_property_type"));
    public static final RegistryKey<Registry<MaterialType<?>>> MATERIAL_TYPE = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("material_type"));
    public static final RegistryKey<Registry<Material>> MATERIAL = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("material"));
    public static final RegistryKey<Registry<MiningLevel>> MINING_LEVEL = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("mining_level"));
    public static final RegistryKey<Registry<PartPropertyType<?>>> PART_PROPERTY_TYPE = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("part_property_type"));
    public static final RegistryKey<Registry<PartType<?>>> PART_TYPE = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("part_type"));
    public static final RegistryKey<Registry<Part>> PART = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("part"));
    public static final RegistryKey<Registry<ToolPropertyType<?>>> TOOL_PROPERTY_TYPE = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("tool_property_type"));
    public static final RegistryKey<Registry<ToolType<?>>> TOOL_TYPE = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("tool_type"));
    public static final RegistryKey<Registry<Tool>> TOOL = RegistryKey
        .ofRegistry(BlueToolsHelper.createIdentifier("tool"));

}
