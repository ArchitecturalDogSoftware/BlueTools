package net.architecturaldog.bluetools.datagen.provider;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

import java.util.List;
import java.util.function.Consumer;

public final class ModelProvider extends FabricModelProvider {

    private final List<Consumer<BlockStateModelGenerator>> blockConsumers = new ObjectArrayList<>();
    private final List<Consumer<ItemModelGenerator>> itemConsumers = new ObjectArrayList<>();

    public ModelProvider(final FabricDataOutput output) {
        super(output);
    }

    public void addBlocks(final Consumer<BlockStateModelGenerator>... consumers) {
        this.blockConsumers.addAll(List.of(consumers));
    }

    public void addItems(final Consumer<ItemModelGenerator>... consumers) {
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
