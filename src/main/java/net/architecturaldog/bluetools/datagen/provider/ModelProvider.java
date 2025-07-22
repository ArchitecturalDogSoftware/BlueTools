package net.architecturaldog.bluetools.datagen.provider;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class ModelProvider extends FabricModelProvider {

    private final @NotNull List<Consumer<BlockStateModelGenerator>> blockConsumers = new ObjectArrayList<>();
    private final @NotNull List<Consumer<ItemModelGenerator>> itemConsumers = new ObjectArrayList<>();

    public ModelProvider(final @NotNull FabricDataOutput output) {
        super(output);
    }

    @SafeVarargs
    public final void addBlocks(final Consumer<BlockStateModelGenerator>... consumers) {
        this.blockConsumers.addAll(List.of(consumers));
    }

    @SafeVarargs
    public final void addItems(final Consumer<ItemModelGenerator>... consumers) {
        this.itemConsumers.addAll(List.of(consumers));
    }

    @Override
    public void generateBlockStateModels(final @NotNull BlockStateModelGenerator blockStateModelGenerator) {
        this.blockConsumers.forEach(consumer -> consumer.accept(blockStateModelGenerator));
    }

    @Override
    public void generateItemModels(final @NotNull ItemModelGenerator itemModelGenerator) {
        this.itemConsumers.forEach(consumer -> consumer.accept(itemModelGenerator));
    }

}
