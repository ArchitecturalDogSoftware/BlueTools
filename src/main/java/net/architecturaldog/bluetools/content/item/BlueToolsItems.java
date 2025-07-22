package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsItems extends AutoLoader {

    public static final @NotNull RegistryLoad<BlockItem> FORGE_INTERFACE =
        BlueToolsItems.create(BlockItem.class, "forge_interface", BlockItem::new, BlueToolsBlocks.FORGE_INTERFACE);

    private static <T extends BlockItem> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.BlockFactory<T> itemFactory,
        final @NotNull Block block
    )
    {
        return BlueToolsItems.create(type, path, itemFactory, block, BlueToolsItemSettings.DEFAULT);
    }

    private static <T extends Item> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.Factory<T> itemFactory
    )
    {
        return BlueToolsItems.create(type, path, itemFactory, BlueToolsItemSettings.DEFAULT);
    }

    private static <T extends BlockItem> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.BlockFactory<T> itemFactory,
        final @NotNull Block block
    )
    {
        return BlueToolsItems.create(type, identifier, itemFactory, block, BlueToolsItemSettings.DEFAULT);
    }

    private static <T extends Item> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.Factory<T> itemFactory
    )
    {
        return BlueToolsItems.create(type, identifier, itemFactory, BlueToolsItemSettings.DEFAULT);
    }

    private static <T extends BlockItem> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.BlockFactory<T> itemFactory,
        final @NotNull Block block,
        final @NotNull BlueToolsItemSettings.Factory settingsFactory
    )
    {
        return BlueToolsItems.create(type, BlueTools.id(path), itemFactory, block, settingsFactory);
    }

    private static <T extends Item> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.Factory<T> itemFactory,
        final @NotNull BlueToolsItemSettings.Factory settingsFactory
    )
    {
        return BlueToolsItems.create(type, BlueTools.id(path), itemFactory, settingsFactory);
    }

    private static <T extends BlockItem> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.BlockFactory<T> itemFactory,
        final @NotNull Block block,
        final @NotNull BlueToolsItemSettings.Factory settingsFactory
    )
    {
        return BlueToolsItems.create(type, identifier, itemFactory.bind(block), settingsFactory);
    }

    private static <T extends Item> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.Factory<T> itemFactory,
        final @NotNull BlueToolsItemSettings.Factory settingsFactory
    )
    {
        return new RegistryLoad<>(
            identifier,
            Registries.ITEM,
            itemFactory.create(settingsFactory.create(type, identifier))
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("items");
    }

    @FunctionalInterface
    public interface Factory<T extends Item> {

        @NotNull T create(final @NotNull Item.Settings settings);

    }

    @FunctionalInterface
    public interface BlockFactory<T extends BlockItem> {

        @NotNull T create(final @NotNull Block block, final @NotNull Item.Settings settings);

        default @NotNull BlueToolsItems.Factory<T> bind(final @NotNull Block block) {
            return settings -> this.create(block, settings);
        }

    }

}
