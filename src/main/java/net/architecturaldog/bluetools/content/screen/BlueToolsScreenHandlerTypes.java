package net.architecturaldog.bluetools.content.screen;

import org.jetbrains.annotations.ApiStatus;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.ClientLoaded;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.screen.custom.ForgeInterfaceScreenHandler;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
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
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class BlueToolsScreenHandlerTypes extends AutoLoader {

    public static final AutoLoaded<ExtendedScreenHandlerType<ForgeInterfaceScreenHandler, ForgeInterfaceScreenHandler.Data>> FORGE_INTERFACE = BlueToolsScreenHandlerTypes
        .create("forge_interface", new ForgeInterfaceScreenHandler.Factory(), ForgeInterfaceScreenHandler.Data.CODEC);

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("screen_handler_types");
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> AutoLoaded<ScreenHandlerType<T>> create(
        final String path,
        final BlueToolsScreenHandlerTypes.Factory<T, U> factory
    )
    {
        return BlueToolsScreenHandlerTypes.create(path, factory, FeatureFlags.VANILLA_FEATURES);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> AutoLoaded<ScreenHandlerType<T>> create(
        final Identifier identifier,
        final BlueToolsScreenHandlerTypes.Factory<T, U> factory
    )
    {
        return BlueToolsScreenHandlerTypes.create(identifier, factory, FeatureFlags.VANILLA_FEATURES);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> AutoLoaded<ScreenHandlerType<T>> create(
        final String path,
        final BlueToolsScreenHandlerTypes.Factory<T, U> factory,
        final FeatureSet featureSet
    )
    {
        return BlueToolsScreenHandlerTypes.create(BlueToolsHelper.createIdentifier(path), factory, featureSet);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> AutoLoaded<ScreenHandlerType<T>> create(
        final Identifier identifier,
        final BlueToolsScreenHandlerTypes.Factory<T, U> factory,
        final FeatureSet featureSet
    )
    {
        final ScreenHandlerType<T> type = new ScreenHandlerType<>(factory, featureSet);

        return new AutoLoaded<>(identifier, type).on(CommonLoaded.class, self -> {
            Registry.register(Registries.SCREEN_HANDLER, self.getLoaderId(), self.getValue());
        }).on(ClientLoaded.class, self -> {
            HandledScreens.register(self.getValue(), factory::createScreen);
        });
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>, D> AutoLoaded<ExtendedScreenHandlerType<T, D>> create(
        final String path,
        final BlueToolsScreenHandlerTypes.ExtendedFactory<T, U, D> factory,
        final PacketCodec<? super RegistryByteBuf, D> codec
    )
    {
        return BlueToolsScreenHandlerTypes.create(BlueToolsHelper.createIdentifier(path), factory, codec);
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>, D> AutoLoaded<ExtendedScreenHandlerType<T, D>> create(
        final Identifier identifier,
        final BlueToolsScreenHandlerTypes.ExtendedFactory<T, U, D> factory,
        final PacketCodec<? super RegistryByteBuf, D> codec
    )
    {
        final ExtendedScreenHandlerType<T, D> type = new ExtendedScreenHandlerType<>(factory, codec);

        return new AutoLoaded<>(identifier, type).on(CommonLoaded.class, self -> {
            Registry.register(Registries.SCREEN_HANDLER, self.getLoaderId(), self.getValue());
        }).on(ClientLoaded.class, self -> {
            HandledScreens.register(self.getValue(), factory::createScreen);
        });
    }

    @ApiStatus.Internal
    public interface Factory<T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> extends ScreenHandlerType.Factory<T> {

        @Environment(EnvType.CLIENT)
        U createScreen(final T handler, final PlayerInventory playerInventory, final Text title);

    }

    @ApiStatus.Internal
    public interface ExtendedFactory<T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>, D> extends ExtendedScreenHandlerType.ExtendedFactory<T, D> {

        @Environment(EnvType.CLIENT)
        U createScreen(final T handler, final PlayerInventory playerInventory, final Text title);

    }

}
