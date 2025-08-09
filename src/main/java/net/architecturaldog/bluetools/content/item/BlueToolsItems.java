package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.ClientLoaded;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsItems extends AutoLoader {

    public static final @NotNull AutoLoaded<BlockItem> FORGE_INTERFACE = BlueToolsItems
        .create(BlockItem.class, "forge_interface", BlockItem::new, BlueToolsBlocks.FORGE_INTERFACE)
        .on(CommonLoaded.class, BlueToolsItems::addToItemGroup);
    public static final @NotNull AutoLoaded<PartItem> PART = BlueToolsItems
        .create(PartItem.class, "part", PartItem::new)
        .on(
            ClientLoaded.class,
            self -> ItemTooltipCallback.EVENT.register(
                (stack, context, type, list) -> self.getValue().getTooltip(stack).ifPresent(text -> list.add(1, text))
            )
        ).on(
            CommonLoaded.class,
            self -> ItemGroupEvents
                .modifyEntriesEvent(BlueToolsItemGroups.registryKey(BlueToolsItemGroups.PARTS))
                .register(entries -> entries.addAll(self.getValue().getValidDefaultStacks()))
        );
    public static final @NotNull AutoLoaded<UpgradedPartItem> UPGRADED_PART = BlueToolsItems
        .create(UpgradedPartItem.class, "upgraded_part", UpgradedPartItem::new)
        .on(
            ClientLoaded.class,
            self -> ItemTooltipCallback.EVENT.register(
                (stack, context, type, list) -> self.getValue().getTooltip(stack).ifPresent(text -> list.add(1, text))
            )
        ).on(
            CommonLoaded.class,
            self -> ItemGroupEvents
                .modifyEntriesEvent(BlueToolsItemGroups.registryKey(BlueToolsItemGroups.PARTS))
                .register(entries -> entries.addAll(self.getValue().getValidDefaultStacks()))
        );

    private static <T extends Item> void addToItemGroup(
        final @NotNull AutoLoaded<T> self
    )
    {
        BlueToolsItems.addToItemGroup(self, BlueToolsItemGroups.DEFAULT);
    }

    private static <T extends Item> void addToItemGroup(
        final @NotNull AutoLoaded<T> self,
        final @NotNull AutoLoaded<ItemGroup> itemGroup
    )
    {
        BlueToolsItems.addToItemGroup(self, BlueToolsItemGroups.registryKey(itemGroup));
    }

    private static <T extends Item> void addToItemGroup(
        final @NotNull AutoLoaded<T> self,
        final @NotNull RegistryKey<ItemGroup> registryKey
    )
    {
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(entries -> entries.add(self.getValue()));
    }

    private static <T extends BlockItem> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.BlockItemFactory<T> itemFactory,
        final @NotNull Block block
    )
    {
        return BlueToolsItems.create(type, path, itemFactory, block, settings -> settings);
    }

    private static <T extends Item> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.ItemFactory<T> itemFactory
    )
    {
        return BlueToolsItems.create(type, path, itemFactory, settings -> settings);
    }

    private static <T extends BlockItem> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.BlockItemFactory<T> itemFactory,
        final @NotNull Block block
    )
    {
        return BlueToolsItems.create(type, identifier, itemFactory, block, settings -> settings);
    }

    private static <T extends Item> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.ItemFactory<T> itemFactory
    )
    {
        return BlueToolsItems.create(type, identifier, itemFactory, settings -> settings);
    }

    private static <T extends BlockItem> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.BlockItemFactory<T> itemFactory,
        final @NotNull Block block,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return BlueToolsItems.create(type, BlueTools.id(path), itemFactory, block, settingsBuilder);
    }

    private static <T extends Item> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsItems.ItemFactory<T> itemFactory,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return BlueToolsItems.create(type, BlueTools.id(path), itemFactory, settingsBuilder);
    }

    private static <T extends BlockItem> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.BlockItemFactory<T> itemFactory,
        final @NotNull Block block,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return BlueToolsItems.create(type, identifier, itemFactory.bind(block), settingsBuilder);
    }

    private static <T extends Item> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsItems.ItemFactory<T> itemFactory,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return new AutoLoaded<>(identifier, itemFactory.create(settingsBuilder.create(type, identifier))).on(
            CommonLoaded.class,
            self -> Registry.register(Registries.ITEM, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("items");
    }

    @FunctionalInterface
    public interface ItemFactory<T extends Item> {

        @NotNull T create(final @NotNull Item.Settings settings);

    }

    @FunctionalInterface
    public interface BlockItemFactory<T extends BlockItem> {

        @NotNull T create(final @NotNull Block block, final @NotNull Item.Settings settings);

        default @NotNull BlueToolsItems.ItemFactory<T> bind(final @NotNull Block block) {
            return settings -> this.create(block, settings);
        }

    }

    @FunctionalInterface
    public interface SettingsBuilder {

        @NotNull Item.Settings build(final @NotNull Item.Settings settings);

        default @NotNull Item.Settings create(
            final @NotNull Class<? extends Item> type,
            final @NotNull Identifier identifier
        )
        {
            final @NotNull RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, identifier);

            @NotNull Item.Settings settings = new Item.Settings().registryKey(registryKey);

            if (BlockItem.class.isAssignableFrom(type)) {
                settings = settings.useBlockPrefixedTranslationKey();
            } else {
                settings = settings.useItemPrefixedTranslationKey();
            }

            return this.build(settings);
        }

    }

}
