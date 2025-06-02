package net.architecturaldog.bluetools.content.material;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

public final class BlueToolsMaterials extends AutoLoader {

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("materials");
    }

}
