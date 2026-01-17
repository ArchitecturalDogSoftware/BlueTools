package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.content.material.MaterialType;
import net.architecturaldog.bluetools.content.material.property.MaterialPropertyType;
import net.architecturaldog.bluetools.content.part.PartType;
import net.architecturaldog.bluetools.content.part.property.PartPropertyType;
import net.architecturaldog.bluetools.content.tool.ToolType;
import net.architecturaldog.bluetools.content.tool.property.ToolPropertyType;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsRegistries extends AutoLoader {

    public static final Registry<MaterialPropertyType<?>> MATERIAL_PROPERTY_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistryKeys.MATERIAL_PROPERTY_TYPE)
        .buildAndRegister();
    public static final Registry<MaterialType<?>> MATERIAL_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistryKeys.MATERIAL_TYPE)
        .buildAndRegister();
    public static final Registry<PartPropertyType<?>> PART_PROPERTY_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistryKeys.PART_PROPERTY_TYPE)
        .buildAndRegister();
    public static final Registry<PartType<?>> PART_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistryKeys.PART_TYPE)
        .buildAndRegister();
    public static final Registry<ToolPropertyType<?>> TOOL_PROPERTY_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistryKeys.TOOL_PROPERTY_TYPE)
        .buildAndRegister();
    public static final Registry<ToolType<?>> TOOL_TYPE = FabricRegistryBuilder
        .createSimple(BlueToolsRegistryKeys.TOOL_TYPE)
        .buildAndRegister();

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("registries");
    }

}
