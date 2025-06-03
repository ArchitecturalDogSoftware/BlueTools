package net.architecturaldog.bluetools.content.resource;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

public final class BlueToolsResourceReloadListeners extends AutoLoader {

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resource_reload_listeners");
    }

}
