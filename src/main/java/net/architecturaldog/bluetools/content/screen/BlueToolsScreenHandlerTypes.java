package net.architecturaldog.bluetools.content.screen;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.ClientLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.screen.custom.ForgeInterfaceScreenHandler;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsScreenHandlerTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<ExtendedScreenHandlerType<ForgeInterfaceScreenHandler,
        ForgeInterfaceScreenHandler.Data>> FORGE_INTERFACE =
        BlueToolsScreenHandlerTypes.create(
            "forge_interface",
            new ForgeInterfaceScreenHandler.Factory(),
            ForgeInterfaceScreenHandler.Data.CODEC
        );

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("screen_handler_types");
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> @NotNull AutoLoaded<ScreenHandlerType<T>> create(
        final @NotNull String path,
        final @NotNull BlueToolsScreenHandlerTypes.Factory<T, U> factory
    )
    {
        return BlueToolsScreenHandlerTypes.create(path, factory, FeatureFlags.VANILLA_FEATURES);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> @NotNull AutoLoaded<ScreenHandlerType<T>> create(
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsScreenHandlerTypes.Factory<T, U> factory
    )
    {
        return BlueToolsScreenHandlerTypes.create(identifier, factory, FeatureFlags.VANILLA_FEATURES);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> @NotNull AutoLoaded<ScreenHandlerType<T>> create(
        final @NotNull String path,
        final @NotNull BlueToolsScreenHandlerTypes.Factory<T, U> factory,
        final @NotNull FeatureSet featureSet
    )
    {
        return BlueToolsScreenHandlerTypes.create(BlueTools.id(path), factory, featureSet);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> @NotNull AutoLoaded<ScreenHandlerType<T>> create(
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsScreenHandlerTypes.Factory<T, U> factory,
        final @NotNull FeatureSet featureSet
    )
    {
        return new RegistryLoaded<>(identifier, Registries.SCREEN_HANDLER, new ScreenHandlerType<>(factory, featureSet))
            .on(ClientLoaded.class, self -> HandledScreens.register(self.getValue(), factory::createScreen));
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>, D> @NotNull AutoLoaded<ExtendedScreenHandlerType<T, D>> create(
        final @NotNull String path,
        final @NotNull BlueToolsScreenHandlerTypes.ExtendedFactory<T, U, D> factory,
        final @NotNull PacketCodec<? super RegistryByteBuf, D> codec
    )
    {
        return BlueToolsScreenHandlerTypes.create(BlueTools.id(path), factory, codec);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>, D> @NotNull AutoLoaded<ExtendedScreenHandlerType<T, D>> create(
        final @NotNull Identifier identifier,
        final @NotNull BlueToolsScreenHandlerTypes.ExtendedFactory<T, U, D> factory,
        final @NotNull PacketCodec<? super RegistryByteBuf, D> codec
    )
    {
        return new RegistryLoaded<>(
            identifier,
            Registries.SCREEN_HANDLER,
            new ExtendedScreenHandlerType<>(factory, codec)
        )
            .on(ClientLoaded.class, self -> HandledScreens.register(self.getValue(), factory::createScreen));
    }

    @ApiStatus.Internal
    public interface Factory<T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>>
        extends ScreenHandlerType.Factory<T>
    {

        @Environment(EnvType.CLIENT)
        @NotNull U createScreen(
            final @NotNull T handler,
            final @NotNull PlayerInventory playerInventory,
            final @NotNull Text title
        );

    }

    @ApiStatus.Internal
    public interface ExtendedFactory<T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>, D>
        extends ExtendedScreenHandlerType.ExtendedFactory<T, D>
    {

        @Environment(EnvType.CLIENT)
        @NotNull U createScreen(
            final @NotNull T handler,
            final @NotNull PlayerInventory playerInventory,
            final @NotNull Text title
        );

    }

}
