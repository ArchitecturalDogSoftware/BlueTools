package net.architecturaldog.bluetools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.BlueToolsContent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public final class BlueTools implements ModInitializer {

    public static final String MOD_ID = "bluetools";
    public static final Logger LOGGER = LoggerFactory.getLogger(BlueTools.class);

    public static Identifier id(final String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        BlueToolsContent.INSTANCE.register(CommonLoaded.class);

        Lodestone.load(CommonLoaded.class, BlueTools.MOD_ID);
    }

}
