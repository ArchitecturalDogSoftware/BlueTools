package net.architecturaldog.bluetools.content.resource;

import com.mojang.serialization.Codec;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public abstract class BlueToolsJsonReloadListener<T> extends JsonDataLoader<T>
    implements CommonLoaded, IdentifiableResourceReloadListener
{

    private final String path;

    protected BlueToolsJsonReloadListener(final String path, final Codec<T> codec, final ResourceFinder finder) {
        super(codec, finder);

        this.path = path;
    }

    protected BlueToolsJsonReloadListener(
        final String path,
        final RegistryWrapper.WrapperLookup registries,
        final Codec<T> codec,
        final RegistryKey<? extends Registry<T>> registryRef
    )
    {
        super(registries, codec, registryRef);

        this.path = path;
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

    @Override
    public Identifier getFabricId() {
        return this.getLoaderId();
    }

    @Override
    public void loadCommon() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
    }

}
