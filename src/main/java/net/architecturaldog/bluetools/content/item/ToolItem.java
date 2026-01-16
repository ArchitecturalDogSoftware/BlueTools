package net.architecturaldog.bluetools.content.item;

import java.util.Map;
import java.util.Optional;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.ToolMaterialsComponent;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.architecturaldog.bluetools.content.tool.property.BlueToolsToolPropertyTypes;
import net.architecturaldog.bluetools.content.tool.property.PartsProperty;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ToolItem extends Item {

    public ToolItem(final Settings settings) {
        super(settings);
    }

    public Optional<Map<String, JsonResourceManager.Entry<Part>>> getParts(final ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL.getValue()))
            .flatMap(
                component -> component.toolEntry().value().getProperty(BlueToolsToolPropertyTypes.PARTS.getValue())
            )
            .map(PartsProperty::parts);
    }

    public Optional<Map<String, JsonResourceManager.Entry<Material>>> getMaterials(final ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL_MATERIALS.getValue()))
            .map(ToolMaterialsComponent::entries);
    }

    @Override
    public Text getName(final ItemStack stack) {
        final String toolTranslationKey = Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL.getValue()))
            .map(component -> component.toolEntry().key().getValue().toTranslationKey("tool"))
            .orElse("tool.missing");

        return Text.translatable(toolTranslationKey);
    }

}
