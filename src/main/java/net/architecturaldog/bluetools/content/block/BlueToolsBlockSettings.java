package net.architecturaldog.bluetools.content.block;

import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsBlockSettings {

    public static final @NotNull BlueToolsBlockSettings.Factory DEFAULT = settings -> settings;

    @FunctionalInterface
    public interface Factory {

        @NotNull AbstractBlock.Settings modify(final @NotNull AbstractBlock.Settings settings);

        default @NotNull BlueToolsBlockSettings.Factory then(final @NotNull BlueToolsBlockSettings.Factory factory) {
            return settings -> factory.modify(this.modify(settings));
        }

        default @NotNull AbstractBlock.Settings create(
            final @NotNull Class<? extends Block> type,
            final @NotNull String path
        )
        {
            return this.create(type, BlueTools.id(path));
        }

        default @NotNull AbstractBlock.Settings create(
            final @NotNull Class<? extends Block> type,
            final @NotNull Identifier identifier
        )
        {
            final @NotNull RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

            @NotNull AbstractBlock.Settings settings = AbstractBlock.Settings.create().registryKey(registryKey);

            if (FluidBlock.class.isAssignableFrom(type)) {
                settings = settings.liquid();
            } else {
                settings = settings.solid();
            }

            return this.modify(settings);
        }

    }

}
