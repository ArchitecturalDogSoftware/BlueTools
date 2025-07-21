package net.architecturaldog.bluetools.utility;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegistryLoad<T> extends Load<T> {

    private final Registry<? super T> registry;

    public RegistryLoad(final Identifier identifier, final Registry<? super T> registry, final T value) {
        super(identifier, value);

        this.registry = registry;
    }

    public RegistryLoad(final String path, final Registry<? super T> registry, final T value) {
        super(path, value);

        this.registry = registry;
    }

    public final Registry<? super T> getRegistry() {
        return this.registry;
    }

    public RegistryLoad<T> onCommonLoad(final Runnable runnable) {
        return (RegistryLoad<T>) super.onCommonLoad(runnable);
    }

    public RegistryLoad<T> onClientLoad(final Runnable runnable) {
        return (RegistryLoad<T>) super.onClientLoad(runnable);
    }

    public RegistryLoad<T> onServerLoad(final Runnable runnable) {
        return (RegistryLoad<T>) super.onServerLoad(runnable);
    }

    public RegistryLoad<T> onDataGenerate(final Runnable runnable) {
        return (RegistryLoad<T>) super.onDataGenerate(runnable);
    }

    @Override
    public void loadCommon() {
        Registry.register(this.getRegistry(), this.getLoaderId(), this.getValue());

        super.loadCommon();
    }

}
