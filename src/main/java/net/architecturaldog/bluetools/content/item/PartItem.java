package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.MaterialComponent;
import net.architecturaldog.bluetools.content.component.PartComponent;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.part.property.BlueToolsPartPropertyTypes;
import net.architecturaldog.bluetools.content.part.property.MaterialValueProperty;
import net.architecturaldog.bluetools.content.part.property.MaterialsProperty;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PartItem extends Item {

    public PartItem(final @NotNull Settings settings) {
        super(settings);
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

    @Override
    public @NotNull Text getName(final @NotNull ItemStack stack) {
        if (!FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            return super.getName(stack);
        }

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
        if (!(stack.getItem() instanceof PartItem)) return Optional.empty();

        final @Nullable MaterialComponent material = stack.get(BlueToolsComponentTypes.MATERIAL.getValue());
        final @Nullable PartComponent part = stack.get(BlueToolsComponentTypes.PART.getValue());

        if (Objects.isNull(material) || Objects.isNull(part)) return Optional.empty();

        return Optional.of(Text.translatable(
            "item.partMaterialValue",
            Text.translatable(material.materialEntry().key().getValue().toTranslationKey("material")),
            part.partEntry().value().getPropertyOr(
                BlueToolsPartPropertyTypes.MATERIAL_VALUE.getValue(),
                MaterialValueProperty.DEFAULT
            ).value().getText()
        ).formatted(Formatting.GRAY));
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
        return BlueToolsResources.PART.getSortedEntries().stream().flatMap(part -> part.value().getPropertyOr(
            BlueToolsPartPropertyTypes.MATERIALS.getValue(),
            MaterialsProperty.DEFAULT
        ).getPermittedMaterials().stream().sorted(BlueToolsResources.MATERIAL.getEntryComparator()).map(
            material -> this.getDefaultStack(part, material)
        )).toList();
    }

}
