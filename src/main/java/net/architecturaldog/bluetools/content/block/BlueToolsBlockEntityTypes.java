package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlockEntity;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class BlueToolsBlockEntityTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<BlockEntityType<ForgeInterfaceBlockEntity>> FORGE_INTERFACE =
        BlueToolsBlockEntityTypes.create(
            "forge_interface",
            ForgeInterfaceBlockEntity::new,
            Set.of(BlueToolsBlocks.FORGE_INTERFACE)
        );

    private static <T extends BlockEntity> @NotNull AutoLoaded<BlockEntityType<T>> create(
        final @NotNull String path,
        final @NotNull BlockEntityType.BlockEntityFactory<T> factory,
        final @NotNull Set<Block> blocks
    )
    {
        return BlueToolsBlockEntityTypes.create(BlueTools.id(path), factory, blocks);
    }

    private static <T extends BlockEntity> @NotNull AutoLoaded<BlockEntityType<T>> create(
        final @NotNull Identifier identifier,
        final @NotNull BlockEntityType.BlockEntityFactory<T> factory,
        final @NotNull Set<Block> blocks
    )
    {
        return new RegistryLoaded<>(identifier, Registries.BLOCK_ENTITY_TYPE, new BlockEntityType<>(factory, blocks));
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("block_entity_types");
    }

}
