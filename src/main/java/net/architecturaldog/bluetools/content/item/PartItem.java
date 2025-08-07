package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.MaterialComponent;
import net.architecturaldog.bluetools.content.component.PartComponent;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PartItem extends Item {

    public PartItem(final Settings settings) {
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
    public Text getName(final ItemStack stack) {
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

}
