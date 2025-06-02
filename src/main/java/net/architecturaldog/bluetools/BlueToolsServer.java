package net.architecturaldog.bluetools;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.ServerLoaded;
import net.architecturaldog.bluetools.content.BlueToolsContent;
import net.fabricmc.api.DedicatedServerModInitializer;

public final class BlueToolsServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        BlueToolsContent.INSTANCE.register(ServerLoaded.class);

        Lodestone.load(ServerLoaded.class, BlueTools.MOD_ID);
    }

}
