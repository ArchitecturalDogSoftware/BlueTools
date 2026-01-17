package net.architecturaldog.bluetools;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.BlueToolsContent;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.fabricmc.api.ModInitializer;

public final class BlueTools implements ModInitializer {

    @Override
    public void onInitialize() {
        BlueToolsContent.INSTANCE.register(CommonLoaded.class);

        Lodestone.load(CommonLoaded.class, BlueToolsHelper.NAMESPACE);
    }

}
