package net.architecturaldog.bluetools.content.item;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.BlueToolsBlocks;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class BlueToolsItems extends AutoLoader {

    public static final RegistryLoaded<BlockItem> FORGE_INTERFACE = create(
        "forge_interface",
        settings -> new BlockItem(BlueToolsBlocks.FORGE_INTERFACE, settings.useBlockPrefixedTranslationKey())
    );

    private static <T extends Item> RegistryLoaded<T> create(final String path, Function<Item.Settings, T> function) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, BlueTools.id(path));

        return new RegistryLoaded<>(
            path,
            Registries.ITEM,
            function.apply(new Item.Settings().registryKey(registryKey))
        );
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("items");
    }

}
