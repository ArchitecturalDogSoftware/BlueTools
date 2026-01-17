package net.architecturaldog.bluetools.content.item;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.MaterialComponent;
import net.architecturaldog.bluetools.content.component.PartComponent;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialValue;
import net.architecturaldog.bluetools.content.material.UpgradeMaterial;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.part.property.BlueToolsPartPropertyTypes;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager.Entry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PartItem extends Item {

    public PartItem(final Settings settings) {
        super(settings);
    }

    protected static Optional<Text> getTooltipForStack(final ItemStack stack) {
        if (!(stack.getItem() instanceof final PartItem item)) return Optional.empty();

        final @Nullable MaterialComponent material = stack.get(BlueToolsComponentTypes.MATERIAL.getValue());
        final @Nullable PartComponent part = stack.get(BlueToolsComponentTypes.PART.getValue());

        if (Objects.isNull(material) || Objects.isNull(part)) return Optional.empty();

        final Text materialText = BlueToolsResources.MATERIAL.getText(material.materialEntry());
        final Text valueText = item.getMaterialValue(stack).orElseGet(() -> new MaterialValue.Ingots(1L)).getText();
        final MutableText tooltipText = Text.translatable("item.partMaterialValue", materialText, valueText);

        return Optional.of(tooltipText.formatted(Formatting.GRAY));
    }

    protected static Stream<ItemStack> getValidDefaultStackStream(
        final Predicate<Entry<Part>> partPredicate,
        final Predicate<Entry<Material>> materialPredicate,
        final BiFunction<Entry<Part>, Entry<Material>, ItemStack> defaultStack
    )
    {
        return BlueToolsResources.MATERIAL.getSortedEntries().stream().filter(materialPredicate).flatMap(material -> {
            final Predicate<Entry<Part>> permitsMaterialPredicate = part -> part
                .value()
                .getPropertyOrDefault(BlueToolsPartPropertyTypes.MATERIALS.getValue())
                .permitsMaterial(material);

            return BlueToolsResources.PART
                .getSortedEntries()
                .stream()
                .filter(permitsMaterialPredicate)
                .filter(partPredicate)
                .sorted(BlueToolsResources.PART.getEntryComparator())
                .map(part -> defaultStack.apply(part, material));
        });
    }

    public Optional<Entry<Part>> getPart(final ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.PART.getValue()))
            .map(PartComponent::partEntry);
    }

    public Optional<Entry<Material>> getMaterial(final ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.MATERIAL.getValue()))
            .map(MaterialComponent::materialEntry);
    }

    public Optional<MaterialValue> getMaterialValue(final ItemStack stack) {
        return this.getPart(stack).map(part -> {
            return part.value().getPropertyOrDefault(BlueToolsPartPropertyTypes.MATERIAL_VALUE.getValue()).value();
        });
    }

    @Override
    public Text getName(final ItemStack stack) {
        return BlueToolsResources.PART
            .getText(this.getPart(stack), BlueToolsResources.MATERIAL.getText(this.getMaterial(stack)));
    }

    public Optional<Text> getTooltip(final ItemStack stack) {
        if (!stack.isOf(BlueToolsItems.PART.getValue())) return Optional.empty();

        return PartItem.getTooltipForStack(stack);
    }

    public ItemStack getDefaultStack(final Entry<Part> partEntry, final Entry<Material> materialEntry) {
        final ItemStack stack = this.getDefaultStack();

        stack.set(BlueToolsComponentTypes.PART.getValue(), new PartComponent(partEntry));
        stack.set(BlueToolsComponentTypes.MATERIAL.getValue(), new MaterialComponent(materialEntry));

        return stack;
    }

    public List<ItemStack> getValidDefaultStacks() {
        final Stream<ItemStack> stackStream = PartItem
            .getValidDefaultStackStream(
                part -> true,
                material -> !(material.value() instanceof UpgradeMaterial),
                this::getDefaultStack
            );

        return stackStream.toList();
    }

}
