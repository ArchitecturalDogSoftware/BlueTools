package net.architecturaldog.bluetools.utility;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RegistryLoad<T> extends Load<T> {

    private final @NotNull Registry<? super T> registry;

    public RegistryLoad(
        final @NotNull String path,
        final @NotNull Registry<? super T> registry,
        final @NotNull T value
    )
    {
        super(path, value);

        this.registry = registry;
    }

    public RegistryLoad(
        final @NotNull Identifier identifier,
        final @NotNull Registry<? super T> registry,
        final @NotNull T value
    )
    {
        super(identifier, value);

        this.registry = registry;
    }

    public final @NotNull Registry<? super T> getRegistry() {
        return this.registry;
    }

    public @NotNull RegistryLoad<T> thenCommon(final @NotNull Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenCommon(consumer);
    }

    public @NotNull RegistryLoad<T> thenClient(final @NotNull Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenClient(consumer);
    }

    public @NotNull RegistryLoad<T> thenServer(final @NotNull Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenServer(consumer);
    }

    public @NotNull RegistryLoad<T> thenGenerate(final @NotNull Consumer<Load<T>> consumer) {
        return (RegistryLoad<T>) super.thenGenerate(consumer);
    }

    @Override
    public void loadCommon() {
        Registry.register(this.getRegistry(), this.getLoaderId(), this.getValue());

        super.loadCommon();
    }

}
