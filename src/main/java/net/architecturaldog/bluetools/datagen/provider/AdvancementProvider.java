package net.architecturaldog.bluetools.datagen.provider;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class AdvancementProvider extends FabricAdvancementProvider {

    private final @NotNull Map<Identifier, Advancement.Builder> builderMap = new Object2ObjectOpenHashMap<>();

    public AdvancementProvider(
        final @NotNull FabricDataOutput output,
        final @NotNull CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup
    )
    {
        super(output, registryLookup);
    }

    public void add(final @NotNull Identifier identifier, final @NotNull Advancement.Builder builder) {
        this.builderMap.put(identifier, builder);
    }

    @Override
    public void generateAdvancement(
        final @NotNull RegistryWrapper.WrapperLookup wrapperLookup,
        final @NotNull Consumer<AdvancementEntry> consumer
    )
    {
        for (final @NotNull Map.Entry<Identifier, Advancement.Builder> entry : this.builderMap.entrySet()) {
            entry.getValue().build(consumer, entry.getKey().toString());
        }
    }

}
