package net.architecturaldog.bluetools.utility;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

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

    public RegistryLoad<T> thenCommon(final Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenCommon(consumer);
    }

    public RegistryLoad<T> thenClient(final Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenClient(consumer);
    }

    public RegistryLoad<T> thenServer(final Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenServer(consumer);
    }

    public RegistryLoad<T> thenGenerate(final Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenGenerate(consumer);
    }

    @Override
    public void loadCommon() {
        Registry.register(this.getRegistry(), this.getLoaderId(), this.getValue());

        super.loadCommon();
    }

}
