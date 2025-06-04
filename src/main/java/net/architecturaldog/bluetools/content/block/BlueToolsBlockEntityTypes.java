package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlockEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public final class BlueToolsBlockEntityTypes extends AutoLoader {

    public static final BlueToolsBlockEntityType<ForgeInterfaceBlockEntity> FORGE_INTERFACE =
        new BlueToolsBlockEntityType<>(
            "forge_interface",
            ForgeInterfaceBlockEntity::new,
            Set.of(BlueToolsBlocks.FORGE_INTERFACE)
        );

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("block_entity_types");
    }

}
