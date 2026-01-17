package net.architecturaldog.bluetools.content.block;

import java.util.Set;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlockEntity;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsBlockEntityTypes extends AutoLoader {

    public static final AutoLoaded<BlockEntityType<ForgeInterfaceBlockEntity>> FORGE_INTERFACE = BlueToolsBlockEntityTypes
        .create("forge_interface", ForgeInterfaceBlockEntity::new, Set.of(BlueToolsBlocks.FORGE_INTERFACE));

    private static <T extends BlockEntity> AutoLoaded<BlockEntityType<T>> create(
        final String path,
        final BlockEntityType.BlockEntityFactory<T> factory,
        final Set<Block> blocks
    )
    {
        return BlueToolsBlockEntityTypes.create(BlueToolsHelper.createIdentifier(path), factory, blocks);
    }

    private static <T extends BlockEntity> AutoLoaded<BlockEntityType<T>> create(
        final Identifier identifier,
        final BlockEntityType.BlockEntityFactory<T> factory,
        final Set<Block> blocks
    )
    {
        return BlueToolsBlockEntityTypes.create(identifier, new BlockEntityType<>(factory, blocks));
    }

    private static <T extends BlockEntity> AutoLoaded<BlockEntityType<T>> create(
        final Identifier identifier,
        final BlockEntityType<T> type
    )
    {
        return new AutoLoaded<>(identifier, type).on(CommonLoaded.class, self -> {
            Registry.register(Registries.BLOCK_ENTITY_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("block_entity_types");
    }

}
