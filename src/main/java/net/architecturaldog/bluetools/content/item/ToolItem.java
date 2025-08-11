package net.architecturaldog.bluetools.content.item;

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
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class ToolItem extends Item {

    public ToolItem(final Settings settings) {
        super(settings);
    }

    public @NotNull Optional<Map<String, JsonResourceManager.Entry<Part>>> getParts(final @NotNull ItemStack stack) {
        return Optional.ofNullable(stack.get(BlueToolsComponentTypes.TOOL.getValue())).flatMap(
            component -> component.toolEntry().value().getProperty(BlueToolsToolPropertyTypes.PARTS.getValue())
        ).map(PartsProperty::parts);
    }

    public @NotNull Optional<Map<String, JsonResourceManager.Entry<Material>>> getMaterials(final @NotNull ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL_MATERIALS.getValue()))
            .map(ToolMaterialsComponent::entries);
    }

    @Override
    public @NotNull Text getName(final @NotNull ItemStack stack) {
        return Text.translatable(
            Optional
                .ofNullable(stack.get(BlueToolsComponentTypes.TOOL.getValue()))
                .map(component -> component.toolEntry().key().getValue().toTranslationKey("tool"))
                .orElse("tool.missing")
        );
    }

}
