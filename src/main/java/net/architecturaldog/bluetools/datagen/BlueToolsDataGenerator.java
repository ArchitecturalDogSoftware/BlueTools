package net.architecturaldog.bluetools.datagen;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.DataGenerating;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.datagen.provider.AdvancementProvider;
import net.architecturaldog.bluetools.datagen.utility.LazyInitializingProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class BlueToolsDataGenerator implements DataGeneratorEntrypoint {

    public static final LazyInitializingProvider<AdvancementProvider> ADVANCEMENT = new LazyInitializingProvider<>();

    @Override
    public void onInitializeDataGenerator(final FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        ADVANCEMENT.set(pack, AdvancementProvider::new);

        Lodestone.load(DataGenerating.class, BlueTools.MOD_ID);
    }

}
