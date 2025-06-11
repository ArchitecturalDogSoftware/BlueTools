package net.architecturaldog.bluetools.content.screen;

import dev.jaxydog.lodestone.api.ClientLoaded;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class BlueToolsScreenHandlerType<T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>>
    extends ScreenHandlerType<T> implements CommonLoaded, ClientLoaded
{

    private final String path;

    public BlueToolsScreenHandlerType(
        final Factory<T> factory,
        final FeatureSet requiredFeatures,
        String path
    )
    {
        super(factory, requiredFeatures);

        this.path = path;
    }

    public BlueToolsScreenHandlerType(final Factory<T> factory, String path) {
        this(factory, FeatureFlags.VANILLA_FEATURES, path);
    }

    @Environment(EnvType.CLIENT)
    protected abstract U create(T handler, PlayerInventory playerInventory, Text title);

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

    @Override
    public void loadCommon() {
        Registry.register(Registries.SCREEN_HANDLER, this.getLoaderId(), this);
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Override
    public void loadClient() {
        HandledScreens.<T, U>register(this, this::create);
    }

}
