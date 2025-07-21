package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class BlueToolsItems extends AutoLoader {

    public static final RegistryLoad<BlockItem> FORGE_INTERFACE = create(
        "forge_interface",
        settings -> new BlockItem(BlueToolsBlocks.FORGE_INTERFACE, settings.useBlockPrefixedTranslationKey())
    );

    private static <T extends Item> RegistryLoad<T> create(final String path, Function<Item.Settings, T> function) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, BlueTools.id(path));
        final T item = function.apply(new Item.Settings().registryKey(registryKey));

        return new RegistryLoad<>(path, Registries.ITEM, item);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("items");
    }

}
