package net.architecturaldog.bluetools.datagen;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.DataGenerating;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsContent;
import net.architecturaldog.bluetools.datagen.provider.AdvancementProvider;
import net.architecturaldog.bluetools.datagen.provider.ModelProvider;
import net.architecturaldog.bluetools.datagen.utility.LazyInitializingProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsDataGenerator implements DataGeneratorEntrypoint {

    public static final @NotNull LazyInitializingProvider<AdvancementProvider> ADVANCEMENT =
        new LazyInitializingProvider<>();
    public static final @NotNull LazyInitializingProvider<ModelProvider> MODEL =
        new LazyInitializingProvider<>();

    @Override
    public void onInitializeDataGenerator(final @NotNull FabricDataGenerator fabricDataGenerator) {
        final @NotNull FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        BlueToolsDataGenerator.ADVANCEMENT.set(pack, AdvancementProvider::new);
        BlueToolsDataGenerator.MODEL.set(pack, ModelProvider::new);

        BlueToolsContent.INSTANCE.register(DataGenerating.class);

        Lodestone.load(DataGenerating.class, BlueTools.MOD_ID);
    }

}
