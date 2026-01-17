package net.architecturaldog.bluetools.datagen;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.DataGenerating;
import net.architecturaldog.bluetools.content.BlueToolsContent;
import net.architecturaldog.bluetools.datagen.provider.AdvancementProvider;
import net.architecturaldog.bluetools.datagen.provider.ModelProvider;
import net.architecturaldog.bluetools.datagen.utility.LazyInitializingProvider;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class BlueToolsDataGenerator implements DataGeneratorEntrypoint {

    public static final LazyInitializingProvider<AdvancementProvider> ADVANCEMENT = new LazyInitializingProvider<>();
    public static final LazyInitializingProvider<ModelProvider> MODEL = new LazyInitializingProvider<>();

    @Override
    public void onInitializeDataGenerator(final FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        BlueToolsDataGenerator.ADVANCEMENT.set(pack, AdvancementProvider::new);
        BlueToolsDataGenerator.MODEL.set(pack, ModelProvider::new);

        BlueToolsContent.INSTANCE.register(DataGenerating.class);

        Lodestone.load(DataGenerating.class, BlueToolsHelper.NAMESPACE);
    }

}
