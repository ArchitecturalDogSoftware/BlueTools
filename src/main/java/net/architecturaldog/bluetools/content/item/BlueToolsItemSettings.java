package net.architecturaldog.bluetools.content.item;

import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsItemSettings {

    public static final @NotNull BlueToolsItemSettings.Factory DEFAULT = settings -> settings;

    @FunctionalInterface
    public interface Factory {

        @NotNull Item.Settings modify(final @NotNull Item.Settings settings);

        default @NotNull BlueToolsItemSettings.Factory then(final @NotNull BlueToolsItemSettings.Factory factory) {
            return settings -> factory.modify(this.modify(settings));
        }

        default @NotNull Item.Settings create(final @NotNull Class<? extends Item> type, final @NotNull String path) {
            return this.create(type, BlueTools.id(path));
        }

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

            return this.modify(settings);
        }

    }

}
