package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsBlocks extends AutoLoader {

    // TODO: Replace this with a `create` call.
    public static final ForgeInterfaceBlock FORGE_INTERFACE = new ForgeInterfaceBlock(
        "forge_interface",
        AbstractBlock.Settings.create()
    );

    private static <T extends FluidBlock> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.FluidBlockFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid
    )
    {
        return BlueToolsBlocks.create(type, path, blockFactory, flowableFluid, settings -> settings);
    }

    private static <T extends Block> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.BlockFactory<T> blockFactory
    )
    {
        return BlueToolsBlocks.create(type, path, blockFactory, settings -> settings);
    }

    private static <T extends FluidBlock> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.FluidBlockFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid
    )
    {
        return BlueToolsBlocks.create(type, identifier, blockFactory, flowableFluid, settings -> settings);
    }

    private static <T extends Block> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.BlockFactory<T> blockFactory
    )
    {
        return BlueToolsBlocks.create(type, identifier, blockFactory, settings -> settings);
    }

    private static <T extends FluidBlock> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.FluidBlockFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return BlueToolsBlocks.create(type, BlueTools.id(path), blockFactory, flowableFluid, settingsBuilder);
    }

    private static <T extends Block> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.BlockFactory<T> blockFactory,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return BlueToolsBlocks.create(type, BlueTools.id(path), blockFactory, settingsBuilder);
    }

    private static <T extends FluidBlock> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.FluidBlockFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return BlueToolsBlocks.create(type, identifier, blockFactory.bind(flowableFluid), settingsBuilder);
    }

    private static <T extends Block> @NotNull AutoLoaded<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.BlockFactory<T> blockFactory,
        final @NotNull SettingsBuilder settingsBuilder
    )
    {
        return new AutoLoaded<>(identifier, blockFactory.create(settingsBuilder.create(type, identifier))).on(
            CommonLoaded.class,
            self -> Registry.register(Registries.BLOCK, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("blocks");
    }

    @FunctionalInterface
    public interface BlockFactory<T extends Block> {

        @NotNull T create(final @NotNull AbstractBlock.Settings settings);

    }

    @FunctionalInterface
    public interface FluidBlockFactory<T extends FluidBlock> {

        @NotNull T create(final @NotNull FlowableFluid flowableFluid, final @NotNull AbstractBlock.Settings settings);

        default @NotNull BlueToolsBlocks.BlockFactory<T> bind(final @NotNull FlowableFluid flowableFluid) {
            return settings -> this.create(flowableFluid, settings);
        }

    }

    @FunctionalInterface
    public interface SettingsBuilder {

        @NotNull AbstractBlock.Settings build(final @NotNull AbstractBlock.Settings settings);

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

            return this.build(settings);
        }

    }

}
