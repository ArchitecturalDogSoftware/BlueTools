package net.architecturaldog.bluetools.content.resource;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
import net.minecraft.util.Identifier;

public final class BlueToolsResourceReloadListeners extends AutoLoader {

    public static final MaterialMiningLevel.Manager MINING_LEVEL_MANAGER = new MaterialMiningLevel.Manager();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resource_reload_listeners");
    }

}
