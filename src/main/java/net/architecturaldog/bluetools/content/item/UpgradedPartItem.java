package net.architecturaldog.bluetools.content.item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.architecturaldog.bluetools.content.material.MaterialValue;
import net.architecturaldog.bluetools.content.material.UpgradeMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class UpgradedPartItem extends PartItem {

    public UpgradedPartItem(final Settings settings) {
        super(settings);
    }

    @Override
    public Optional<MaterialValue> getMaterialValue(final ItemStack stack) {
        return Optional.of(new MaterialValue.Ingots(1L));
    }

    public Optional<Text> getTooltip(final ItemStack stack) {
        if (!stack.isOf(BlueToolsItems.UPGRADED_PART.getValue())) return Optional.empty();

        return UpgradedPartItem.getTooltipForStack(stack);
    }

    @Override
    public List<ItemStack> getValidDefaultStacks() {
        final Stream<ItemStack> stackStream = UpgradedPartItem
            .getValidDefaultStackStream(
                part -> true,
                material -> material.value() instanceof UpgradeMaterial,
                this::getDefaultStack
            );

        return stackStream.toList();
    }

}
