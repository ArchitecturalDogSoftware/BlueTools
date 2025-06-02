package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

public class BlueToolsBlockEntityTypes extends AutoLoader {

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("block_entity_types");
    }

}
