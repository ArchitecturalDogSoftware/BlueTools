package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.MaterialComponent;
import net.architecturaldog.bluetools.content.component.PartComponent;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialValue;
import net.architecturaldog.bluetools.content.material.UpgradeMaterial;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.part.property.BlueToolsPartPropertyTypes;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PartItem extends Item {

    public PartItem(final @NotNull Settings settings) {
        super(settings);
    }

    protected static @NotNull Optional<Text> getTooltipForStack(final @NotNull ItemStack stack) {
        if (!(stack.getItem() instanceof final PartItem item)) return Optional.empty();

        final @Nullable MaterialComponent material = stack.get(BlueToolsComponentTypes.MATERIAL.getValue());
        final @Nullable PartComponent part = stack.get(BlueToolsComponentTypes.PART.getValue());

        if (Objects.isNull(material) || Objects.isNull(part)) return Optional.empty();

        return Optional.of(Text.translatable(
            "item.partMaterialValue",
            Text.translatable(material.materialEntry().key().getValue().toTranslationKey("material")),
            item.getMaterialValue(stack).orElseGet(() -> new MaterialValue.Ingots(1L)).getText()
        ).formatted(Formatting.GRAY));
    }

    protected static @NotNull Stream<ItemStack> getValidDefaultStackStream(
        final @NotNull Predicate<JsonResourceManager.Entry<Part>> partPredicate,
        final @NotNull Predicate<JsonResourceManager.Entry<Material>> materialPredicate,
        final @NotNull BiFunction<JsonResourceManager.Entry<Part>, JsonResourceManager.Entry<Material>, ItemStack> defaultStack
    )
    {
        return BlueToolsResources.MATERIAL.getSortedEntries().stream().filter(materialPredicate).flatMap(
            material -> BlueToolsResources.PART.getSortedEntries().stream().filter(
                part -> part.value().getPropertyOrDefault(
                    BlueToolsPartPropertyTypes.MATERIALS.getValue()
                ).permitsMaterial(material)
            ).filter(partPredicate).sorted(BlueToolsResources.PART.getEntryComparator()).map(
                part -> defaultStack.apply(part, material)
            )
        );
    }

    public @NotNull Optional<Part> getPart(final @NotNull ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.PART.getValue()))
            .map(component -> component.partEntry().value());
    }

    public @NotNull Optional<Material> getMaterial(final @NotNull ItemStack stack) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.MATERIAL.getValue()))
            .map(component -> component.materialEntry().value());
    }

    public @NotNull Optional<MaterialValue> getMaterialValue(final @NotNull ItemStack stack) {
        return this.getPart(stack).map(
            part -> part.getPropertyOrDefault(BlueToolsPartPropertyTypes.MATERIAL_VALUE.getValue()).value()
        );
    }

    @Override
    public @NotNull Text getName(final @NotNull ItemStack stack) {
        return Text.translatable(
            Optional
                .ofNullable(stack.get(BlueToolsComponentTypes.PART.getValue()))
                .map(component -> component.partEntry().key().getValue().toTranslationKey("part"))
                .orElse("part.missing"),
            Text.translatable(Optional
                .ofNullable(stack.get(BlueToolsComponentTypes.MATERIAL.getValue()))
                .map(component -> component.materialEntry().key().getValue().toTranslationKey("material"))
                .orElse("material.missing"))
        );
    }

    public @NotNull Optional<Text> getTooltip(final @NotNull ItemStack stack) {
        if (!stack.isOf(BlueToolsItems.PART.getValue())) return Optional.empty();

        return PartItem.getTooltipForStack(stack);
    }

    public @NotNull ItemStack getDefaultStack(
        final @NotNull JsonResourceManager.Entry<Part> partEntry,
        final @NotNull JsonResourceManager.Entry<Material> materialEntry
    )
    {
        final @NotNull ItemStack stack = this.getDefaultStack();

        stack.set(BlueToolsComponentTypes.PART.getValue(), new PartComponent(partEntry));
        stack.set(BlueToolsComponentTypes.MATERIAL.getValue(), new MaterialComponent(materialEntry));

        return stack;
    }

    public @NotNull List<ItemStack> getValidDefaultStacks() {
        return PartItem.getValidDefaultStackStream(
            part -> true,
            material -> !(material.value() instanceof UpgradeMaterial),
            this::getDefaultStack
        ).toList();
    }

}
