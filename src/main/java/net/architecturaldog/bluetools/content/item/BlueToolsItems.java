package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

public final class BlueToolsItems extends AutoLoader {

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("items");
    }

}
