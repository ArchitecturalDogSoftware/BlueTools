package net.architecturaldog.bluetools.content.block;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlock;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsBlocks extends AutoLoader {

    // TODO: Replace this with a `create` call.
    public static final ForgeInterfaceBlock FORGE_INTERFACE = new ForgeInterfaceBlock(
        "forge_interface",
        AbstractBlock.Settings.create()
    );

    private static <T extends FluidBlock> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.FluidFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid
    )
    {
        return BlueToolsBlocks.create(type, path, blockFactory, flowableFluid, BlueToolsBlockSettings.DEFAULT);
    }

    private static <T extends Block> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.Factory<T> blockFactory
    )
    {
        return BlueToolsBlocks.create(type, path, blockFactory, BlueToolsBlockSettings.DEFAULT);
    }

    private static <T extends FluidBlock> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.FluidFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid
    )
    {
        return BlueToolsBlocks.create(type, identifier, blockFactory, flowableFluid, BlueToolsBlockSettings.DEFAULT);
    }

    private static <T extends Block> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.Factory<T> blockFactory
    )
    {
        return BlueToolsBlocks.create(type, identifier, blockFactory, BlueToolsBlockSettings.DEFAULT);
    }

    private static <T extends FluidBlock> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.FluidFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid,
        final @NotNull BlueToolsBlockSettings.Factory settingsFactory
    )
    {
        return BlueToolsBlocks.create(type, BlueTools.id(path), blockFactory, flowableFluid, settingsFactory);
    }

    private static <T extends Block> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull String path,
        final @NotNull BlueToolsBlocks.Factory<T> blockFactory,
        final @NotNull BlueToolsBlockSettings.Factory settingsFactory
    )
    {
        return BlueToolsBlocks.create(type, BlueTools.id(path), blockFactory, settingsFactory);
    }

    private static <T extends FluidBlock> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.FluidFactory<T> blockFactory,
        final @NotNull FlowableFluid flowableFluid,
        final @NotNull BlueToolsBlockSettings.Factory settingsFactory
    )
    {
        return BlueToolsBlocks.create(type, identifier, blockFactory.bind(flowableFluid), settingsFactory);
    }

    private static <T extends Block> @NotNull RegistryLoad<T> create(
        final @NotNull Class<T> type,
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsBlocks.Factory<T> blockFactory,
        final @NotNull BlueToolsBlockSettings.Factory settingsFactory
    )
    {
        return new RegistryLoad<>(
            identifier,
            Registries.BLOCK,
            blockFactory.create(settingsFactory.create(type, identifier))
        );
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("blocks");
    }

    @FunctionalInterface
    public interface Factory<T extends Block> {

        @NotNull T create(final @NotNull AbstractBlock.Settings settings);

    }

    @FunctionalInterface
    public interface FluidFactory<T extends FluidBlock> {

        @NotNull T create(final @NotNull FlowableFluid flowableFluid, final @NotNull AbstractBlock.Settings settings);

        default @NotNull BlueToolsBlocks.Factory<T> bind(final @NotNull FlowableFluid flowableFluid) {
            return settings -> this.create(flowableFluid, settings);
        }

    }

}
