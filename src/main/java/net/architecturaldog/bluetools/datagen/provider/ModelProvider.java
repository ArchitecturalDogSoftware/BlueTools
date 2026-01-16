package net.architecturaldog.bluetools.datagen.provider;

import java.util.List;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public final class ModelProvider extends FabricModelProvider {

    private final List<Consumer<BlockStateModelGenerator>> blockConsumers = new ObjectArrayList<>();
    private final List<Consumer<ItemModelGenerator>> itemConsumers = new ObjectArrayList<>();

    public ModelProvider(final FabricDataOutput output) {
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
    public void generateBlockStateModels(final BlockStateModelGenerator blockStateModelGenerator) {
        this.blockConsumers.forEach(consumer -> consumer.accept(blockStateModelGenerator));
    }

    @Override
    public void generateItemModels(final ItemModelGenerator itemModelGenerator) {
        this.itemConsumers.forEach(consumer -> consumer.accept(itemModelGenerator));
    }

}
