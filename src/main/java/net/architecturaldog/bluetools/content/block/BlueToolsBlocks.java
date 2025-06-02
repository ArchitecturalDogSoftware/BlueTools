package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

public final class BlueToolsBlocks extends AutoLoader {

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("blocks");
    }

}
