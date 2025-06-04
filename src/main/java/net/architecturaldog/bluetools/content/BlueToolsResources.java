package net.architecturaldog.bluetools.content;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
import net.minecraft.util.Identifier;

public final class BlueToolsResources extends AutoLoader {

    public static final MaterialMiningLevel.Manager MINING_LEVEL = new MaterialMiningLevel.Manager();

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

}
