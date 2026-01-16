package net.architecturaldog.bluetools.datagen.provider;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public final class AdvancementProvider extends FabricAdvancementProvider {

    private final Map<Identifier, Advancement.Builder> builderMap = new Object2ObjectOpenHashMap<>();

    public AdvancementProvider(
        final FabricDataOutput output,
        final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup
    )
    {
        super(output, registryLookup);
    }

    public void add(final Identifier identifier, final Advancement.Builder builder) {
        this.builderMap.put(identifier, builder);
    }

    @Override
    public void generateAdvancement(
        final RegistryWrapper.WrapperLookup wrapperLookup,
        final Consumer<AdvancementEntry> consumer
    )
    {
        for (final Map.Entry<Identifier, Advancement.Builder> entry : this.builderMap.entrySet()) {
            entry.getValue().build(consumer, entry.getKey().toString());
        }
    }

}
