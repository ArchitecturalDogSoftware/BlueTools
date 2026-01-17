package net.architecturaldog.bluetools.content.block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.block.custom.ForgeInterfaceBlock;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class BlueToolsBlocks extends AutoLoader {

    // TODO: Replace this with a `create` call.
    public static final ForgeInterfaceBlock FORGE_INTERFACE = new ForgeInterfaceBlock(
        "forge_interface",
        AbstractBlock.Settings.create()
    );

    private static <T extends FluidBlock> AutoLoaded<T> create(
        final String path,
        final BiFunction<FlowableFluid, AbstractBlock.Settings, T> factory,
        final FlowableFluid fluid
    )
    {
        return BlueToolsBlocks.create(BlueToolsHelper.createIdentifier(path), factory, fluid);
    }

    private static <T extends FluidBlock> AutoLoaded<T> create(
        final Identifier identifier,
        final BiFunction<FlowableFluid, AbstractBlock.Settings, T> factory,
        final FlowableFluid fluid
    )
    {
        return BlueToolsBlocks.create(identifier, factory, fluid, UnaryOperator.identity());
    }

    private static <T extends FluidBlock> AutoLoaded<T> create(
        final String path,
        final BiFunction<FlowableFluid, AbstractBlock.Settings, T> factory,
        final FlowableFluid fluid,
        final UnaryOperator<AbstractBlock.Settings> settingsBuilder
    )
    {
        return BlueToolsBlocks.create(BlueToolsHelper.createIdentifier(path), factory, fluid, settingsBuilder);
    }

    private static <T extends FluidBlock> AutoLoaded<T> create(
        final Identifier identifier,
        final BiFunction<FlowableFluid, AbstractBlock.Settings, T> factory,
        final FlowableFluid fluid,
        final UnaryOperator<AbstractBlock.Settings> settingsBuilder
    )
    {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        final AbstractBlock.Settings settings = AbstractBlock.Settings.create().registryKey(registryKey).liquid();

        return BlueToolsBlocks.create(identifier, factory.apply(fluid, settingsBuilder.apply(settings)));
    }

    private static <T extends Block> AutoLoaded<T> create(
        final String path,
        final Function<AbstractBlock.Settings, T> factory
    )
    {
        return BlueToolsBlocks.create(BlueToolsHelper.createIdentifier(path), factory);
    }

    private static <T extends Block> AutoLoaded<T> create(
        final Identifier identifier,
        final Function<AbstractBlock.Settings, T> factory
    )
    {
        return BlueToolsBlocks.create(identifier, factory, UnaryOperator.identity());
    }

    private static <T extends Block> AutoLoaded<T> create(
        final String path,
        final Function<AbstractBlock.Settings, T> factory,
        final UnaryOperator<AbstractBlock.Settings> settingsBuilder
    )
    {
        return BlueToolsBlocks.create(BlueToolsHelper.createIdentifier(path), factory, settingsBuilder);
    }

    private static <T extends Block> AutoLoaded<T> create(
        final Identifier identifier,
        final Function<AbstractBlock.Settings, T> factory,
        final UnaryOperator<AbstractBlock.Settings> settingsBuilder
    )
    {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        final AbstractBlock.Settings settings = AbstractBlock.Settings.create().registryKey(registryKey);

        return BlueToolsBlocks.create(identifier, factory.apply(settingsBuilder.apply(settings)));
    }

    private static <T extends Block> AutoLoaded<T> create(final String path, final T block) {
        return BlueToolsBlocks.create(BlueToolsHelper.createIdentifier(path), block);
    }

    private static <T extends Block> AutoLoaded<T> create(final Identifier identifier, final T block) {
        return new AutoLoaded<>(identifier, block).on(CommonLoaded.class, self -> {
            Registry.register(Registries.BLOCK, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("blocks");
    }

}
