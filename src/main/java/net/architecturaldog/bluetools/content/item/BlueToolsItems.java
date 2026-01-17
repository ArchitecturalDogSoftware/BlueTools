package net.architecturaldog.bluetools.content.item;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.ClientLoaded;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
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

public final class BlueToolsItems extends AutoLoader {

    public static final AutoLoaded<BlockItem> FORGE_INTERFACE = BlueToolsItems
        .create("forge_interface", BlockItem::new, BlueToolsBlocks.FORGE_INTERFACE)
        .on(CommonLoaded.class, BlueToolsItems::appendToGroup);
    public static final AutoLoaded<PartItem> PART = BlueToolsItems
        .createPart("part", PartItem::new);
    public static final AutoLoaded<UpgradedPartItem> UPGRADED_PART = BlueToolsItems
        .createPart("upgraded_part", UpgradedPartItem::new);
    public static final AutoLoaded<ToolItem> TOOL = BlueToolsItems
        .createTool("tool", ToolItem::new);

    private static <T extends Item> void appendToGroup(final AutoLoaded<T> self) {
        BlueToolsItems.appendToGroup(self, BlueToolsItemGroups.DEFAULT);
    }

    private static <T extends Item> void appendToGroup(
        final AutoLoaded<T> self,
        final AutoLoaded<ItemGroup> itemGroup
    )
    {
        BlueToolsItems.appendToGroup(self, RegistryKey.of(RegistryKeys.ITEM_GROUP, itemGroup.getLoaderId()));
    }

    private static <T extends Item> void appendToGroup(
        final AutoLoaded<T> self,
        final RegistryKey<ItemGroup> registryKey
    )
    {
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(entries -> entries.add(self.getValue()));
    }

    private static <T extends ToolItem> AutoLoaded<T> createTool(
        final String path,
        final Function<Item.Settings, T> factory
    )
    {
        return BlueToolsItems.createTool(BlueToolsHelper.createIdentifier(path), factory);
    }

    private static <T extends ToolItem> AutoLoaded<T> createTool(
        final Identifier identifier,
        final Function<Item.Settings, T> factory
    )
    {
        return BlueToolsItems.createTool(identifier, factory, UnaryOperator.identity());
    }

    private static <T extends ToolItem> AutoLoaded<T> createTool(
        final String path,
        final Function<Item.Settings, T> factory,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        return BlueToolsItems.createTool(BlueToolsHelper.createIdentifier(path), factory, settingsBuilder);
    }

    private static <T extends ToolItem> AutoLoaded<T> createTool(
        final Identifier identifier,
        final Function<Item.Settings, T> factory,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        return BlueToolsItems
            .create(identifier, factory, settingsBuilder)
            .on(
                ClientLoaded.class,
                self -> ItemTooltipCallback.EVENT.register((stack, context, type, list) ->
                {
                    self.getValue().getTooltips(stack).ifPresent(text -> list.addAll(1, text));
                })
            )
            .on(
                CommonLoaded.class,
                self -> ItemGroupEvents
                    .modifyEntriesEvent(BlueToolsItemGroups.registryKey(BlueToolsItemGroups.TOOLS))
                    .register(entries -> entries.addAll(self.getValue().getValidDefaultStacks()))
            );
    }

    private static <T extends PartItem> AutoLoaded<T> createPart(
        final String path,
        final Function<Item.Settings, T> factory
    )
    {
        return BlueToolsItems.createPart(BlueToolsHelper.createIdentifier(path), factory);
    }

    private static <T extends PartItem> AutoLoaded<T> createPart(
        final Identifier identifier,
        final Function<Item.Settings, T> factory
    )
    {
        return BlueToolsItems.createPart(identifier, factory, UnaryOperator.identity());
    }

    private static <T extends PartItem> AutoLoaded<T> createPart(
        final String path,
        final Function<Item.Settings, T> factory,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        return BlueToolsItems.createPart(BlueToolsHelper.createIdentifier(path), factory, settingsBuilder);
    }

    private static <T extends PartItem> AutoLoaded<T> createPart(
        final Identifier identifier,
        final Function<Item.Settings, T> factory,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        return BlueToolsItems
            .create(identifier, factory, settingsBuilder)
            .on(
                ClientLoaded.class,
                self -> ItemTooltipCallback.EVENT.register((stack, context, type, list) ->
                {
                    self.getValue().getTooltip(stack).ifPresent(text -> list.add(1, text));
                })
            )
            .on(
                CommonLoaded.class,
                self -> ItemGroupEvents
                    .modifyEntriesEvent(BlueToolsItemGroups.registryKey(BlueToolsItemGroups.PARTS))
                    .register(entries -> entries.addAll(self.getValue().getValidDefaultStacks()))
            );
    }

    private static <T extends BlockItem> AutoLoaded<T> create(
        final String path,
        final BiFunction<Block, Item.Settings, T> factory,
        final Block block
    )
    {
        return BlueToolsItems.create(BlueToolsHelper.createIdentifier(path), factory, block);
    }

    private static <T extends BlockItem> AutoLoaded<T> create(
        final Identifier identifier,
        final BiFunction<Block, Item.Settings, T> factory,
        final Block block
    )
    {
        return BlueToolsItems.create(identifier, factory, block, UnaryOperator.identity());
    }

    private static <T extends BlockItem> AutoLoaded<T> create(
        final String path,
        final BiFunction<Block, Item.Settings, T> factory,
        final Block block,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        return BlueToolsItems.create(BlueToolsHelper.createIdentifier(path), factory, block, settingsBuilder);
    }

    private static <T extends BlockItem> AutoLoaded<T> create(
        final Identifier identifier,
        final BiFunction<Block, Item.Settings, T> factory,
        final Block block,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, identifier);
        final Item.Settings settings = new Item.Settings().registryKey(registryKey).useBlockPrefixedTranslationKey();

        return BlueToolsItems.create(identifier, factory.apply(block, settingsBuilder.apply(settings)));
    }

    private static <T extends Item> AutoLoaded<T> create(final String path, final Function<Item.Settings, T> factory) {
        return BlueToolsItems.create(BlueToolsHelper.createIdentifier(path), factory);
    }

    private static <T extends Item> AutoLoaded<T> create(
        final Identifier identifier,
        final Function<Item.Settings, T> factory
    )
    {
        return BlueToolsItems.create(identifier, factory, UnaryOperator.identity());
    }

    private static <T extends Item> AutoLoaded<T> create(
        final String path,
        final Function<Item.Settings, T> factory,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        return BlueToolsItems.create(BlueToolsHelper.createIdentifier(path), factory, settingsBuilder);
    }

    private static <T extends Item> AutoLoaded<T> create(
        final Identifier identifier,
        final Function<Item.Settings, T> factory,
        final UnaryOperator<Item.Settings> settingsBuilder
    )
    {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, identifier);
        final Item.Settings settings = new Item.Settings().registryKey(registryKey).useItemPrefixedTranslationKey();

        return BlueToolsItems.create(identifier, factory.apply(settingsBuilder.apply(settings)));
    }

    private static <T extends Item> AutoLoaded<T> create(final String path, final T item) {
        return BlueToolsItems.create(BlueToolsHelper.createIdentifier(path), item);
    }

    private static <T extends Item> AutoLoaded<T> create(final Identifier identifier, final T item) {
        return new AutoLoaded<>(identifier, item).on(CommonLoaded.class, self -> {
            Registry.register(Registries.ITEM, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("items");
    }

}
