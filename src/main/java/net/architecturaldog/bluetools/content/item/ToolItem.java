package net.architecturaldog.bluetools.content.item;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.ToolComponent;
import net.architecturaldog.bluetools.content.component.ToolMaterialsComponent;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.part.property.BlueToolsPartPropertyTypes;
import net.architecturaldog.bluetools.content.part.property.MaterialsProperty;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager.Entry;
import net.architecturaldog.bluetools.content.tool.Tool;
import net.architecturaldog.bluetools.content.tool.property.BlueToolsToolPropertyTypes;
import net.architecturaldog.bluetools.content.tool.property.PartsProperty;
import net.architecturaldog.bluetools.utility.MapCombinations;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ToolItem extends Item {

    public ToolItem(final Settings settings) {
        super(settings);
    }

    protected static Stream<ItemStack> getValidDefaultStackStream(
        final Predicate<Entry<Tool>> toolPredicate,
        final Predicate<Entry<Material>> materialPredicate,
        final BiFunction<Entry<Tool>, Map<String, Entry<Material>>, ItemStack> defaultStack
    )
    {
        return BlueToolsResources.TOOL.streamSortedEntries().filter(toolPredicate).flatMap(toolEntry -> {
            final Optional<Map<String, Entry<Part>>> partEntries = toolEntry
                .value()
                .getProperty(BlueToolsToolPropertyTypes.PARTS.getValue())
                .map(PartsProperty::parts)
                .filter(map -> !map.isEmpty());

            if (partEntries.isEmpty()) return Stream.empty();

            final Map<String, List<Entry<Material>>> validValues = partEntries.get().entrySet().stream().map(entry -> {
                final List<Entry<Material>> values = entry
                    .getValue()
                    .value()
                    .getPropertyOrDefault(BlueToolsPartPropertyTypes.MATERIALS.getValue())
                    .getPermittedMaterials();

                return Map.entry(entry.getKey(), values);
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new MapCombinations<>(validValues, Comparator.naturalOrder()).stream().map(materialEntries -> {
                return defaultStack.apply(toolEntry, materialEntries);
            });
        });
    }

    protected static Optional<List<Text>> getTooltipsForStack(final ItemStack stack) {
        if (!(stack.getItem() instanceof final ToolItem item)) return Optional.empty();

        final Optional<PartsProperty> parts = item.getParts(stack);
        final Map<String, Entry<Material>> materials = item.getMaterials(stack).orElseGet(Map::of);

        if (parts.isEmpty() || materials.isEmpty()) return Optional.of(List.of());

        return Optional.of(parts.get().parts().entrySet().stream().map(entry -> {
            final String materialTranslationKey = Optional
                .ofNullable(materials.get(entry.getKey()))
                .map(material -> material.key().getValue().toTranslationKey("material"))
                .orElse("material.empty");
            final String partTranslationKey = entry.getValue().key().getValue().toTranslationKey("part");

            return (Text) Text
                .translatable(partTranslationKey, Text.translatable(materialTranslationKey))
                .formatted(Formatting.GRAY);
        }).toList());
    }

    public Optional<Entry<Tool>> getTool(final ItemStack stack) {
        return Optional.ofNullable(stack.get(BlueToolsComponentTypes.TOOL.getValue())).map(ToolComponent::toolEntry);
    }

    public Optional<PartsProperty> getParts(final ItemStack stack) {
        return this
            .getTool(stack)
            .flatMap(entry -> entry.value().getProperty(BlueToolsToolPropertyTypes.PARTS.getValue()));
    }

    public Optional<Map<String, Entry<Material>>> getMaterials(final ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL_MATERIALS.getValue()))
            .map(ToolMaterialsComponent::entries);
    }

    public Optional<Entry<Material>> getMaterial(final ItemStack stack, final String partKey) {
        return this.getMaterials(stack).flatMap(materials -> Optional.ofNullable(materials.get(partKey)));
    }

    @Override
    public Text getName(final ItemStack stack) {
        final String toolTranslationKey = Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL.getValue()))
            .map(component -> component.toolEntry().key().getValue().toTranslationKey("tool"))
            .orElse("tool.missing");

        final String materialTranslationKey = this
            .getParts(stack)
            .map(PartsProperty::headKey)
            .flatMap(key -> this.getMaterial(stack, key))
            .map(material -> material.key().getValue().toTranslationKey("material"))
            .orElse("material.missing");

        return Text.translatable(toolTranslationKey, Text.translatable(materialTranslationKey));
    }

    public ItemStack getDefaultStack(final Entry<Tool> toolEntry, final Map<String, Entry<Material>> materialEntries) {
        final Map<String, Entry<Part>> partEntries = toolEntry
            .value()
            .getProperty(BlueToolsToolPropertyTypes.PARTS.getValue())
            .map(PartsProperty::parts)
            .orElseGet(Map::of);

        for (final String partKey : partEntries.keySet()) {
            if (!materialEntries.containsKey(partKey)) {
                throw new IllegalArgumentException("Material for part '%s' must be set".formatted(partKey));
            }

            final Entry<Part> partEntry = partEntries.get(partKey);
            final Entry<Material> materialEntry = materialEntries.get(partKey);
            final MaterialsProperty partMaterialsProperty = partEntry
                .value()
                .getPropertyOrDefault(BlueToolsPartPropertyTypes.MATERIALS.getValue());

            if (!partMaterialsProperty.permitsMaterial(materialEntry)) {
                throw new IllegalArgumentException(
                    "Material for part '%s' cannot be '%s'".formatted(partKey, materialEntry.key())
                );
            }
        }

        final ItemStack stack = this.getDefaultStack();

        stack.set(BlueToolsComponentTypes.TOOL.getValue(), new ToolComponent(toolEntry));
        stack.set(BlueToolsComponentTypes.TOOL_MATERIALS.getValue(), new ToolMaterialsComponent(materialEntries));

        return stack;
    }

    public Optional<List<Text>> getTooltips(final ItemStack stack) {
        if (!stack.isOf(BlueToolsItems.TOOL.getValue())) return Optional.empty();

        return ToolItem.getTooltipsForStack(stack);
    }

    public List<ItemStack> getValidDefaultStacks() {
        return ToolItem.getValidDefaultStackStream(tool -> true, material -> true, this::getDefaultStack).toList();
    }

}
