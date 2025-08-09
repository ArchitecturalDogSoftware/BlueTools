package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.content.material.MaterialValue;
import net.architecturaldog.bluetools.content.material.UpgradeMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class UpgradedPartItem extends PartItem {

    public UpgradedPartItem(final @NotNull Settings settings) {
        super(settings);
    }

    @Override
    public @NotNull Optional<MaterialValue> getMaterialValue(final @NotNull ItemStack stack) {
        return Optional.of(new MaterialValue.Ingots(1L));
    }

    public @NotNull Optional<Text> getTooltip(final @NotNull ItemStack stack) {
        if (!stack.isOf(BlueToolsItems.UPGRADED_PART.getValue())) return Optional.empty();

        return UpgradedPartItem.getTooltipForStack(stack);
    }

    @Override
    public @NotNull List<ItemStack> getValidDefaultStacks() {
        return UpgradedPartItem.getValidDefaultStackStream(
            part -> true,
            material -> material.value() instanceof UpgradeMaterial,
            this::getDefaultStack
        ).toList();
    }

}
