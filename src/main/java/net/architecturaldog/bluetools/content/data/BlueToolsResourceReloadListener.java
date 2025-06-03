package net.architecturaldog.bluetools.content.data;

import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

public abstract class BlueToolsResourceReloadListener implements CommonLoaded, IdentifiableResourceReloadListener {

    private final ResourceType type;
    private final String path;

    protected BlueToolsResourceReloadListener(final ResourceType type, final String path) {
        this.type = type;
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
        ResourceManagerHelper.get(this.type).registerReloadListener(this);
    }

    public static abstract class Synchronous extends BlueToolsResourceReloadListener
        implements SynchronousResourceReloader
    {

        protected Synchronous(final ResourceType type, final String path) {
            super(type, path);
        }

    }

}
