package net.architecturaldog.bluetools.content.component;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

public final class BlueToolsComponentTypes extends AutoLoader {

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("component_types");
    }

}
