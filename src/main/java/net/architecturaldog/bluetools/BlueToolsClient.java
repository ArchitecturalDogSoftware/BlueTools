package net.architecturaldog.bluetools;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.ClientLoaded;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class BlueToolsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Lodestone.load(ClientLoaded.class, BlueTools.MOD_ID);
    }

}
