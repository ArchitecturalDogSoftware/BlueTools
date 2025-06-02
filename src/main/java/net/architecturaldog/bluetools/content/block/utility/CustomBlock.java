package net.architecturaldog.bluetools.content.block.utility;

import dev.jaxydog.lodestone.api.CommonLoaded;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface CustomBlock extends CommonLoaded, BlockConvertible {

    @Override
    default void loadCommon() {
        Registry.register(Registries.BLOCK, this.getLoaderId(), this.asBlock());
    }

}
