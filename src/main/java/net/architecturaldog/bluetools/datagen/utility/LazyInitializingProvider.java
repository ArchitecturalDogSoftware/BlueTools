package net.architecturaldog.bluetools.datagen.utility;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public final class LazyInitializingProvider<T extends DataProvider> {

    private @Nullable Supplier<T> supplier;
    private @Nullable T provider;

    public LazyInitializingProvider() {

    }

    public void set(
        final @NotNull FabricDataGenerator.Pack pack,
        final @NotNull FabricDataGenerator.Pack.Factory<T> factory
    )
    {
        if (Objects.nonNull(this.supplier)) {
            throw new IllegalStateException("The provider has already been set");
        }

        this.supplier = () -> pack.addProvider(factory);
    }

    public void set(
        final @NotNull FabricDataGenerator.Pack pack,
        final @NotNull FabricDataGenerator.Pack.RegistryDependentFactory<T> factory
    )
    {
        if (Objects.nonNull(this.supplier)) {
            throw new IllegalStateException("The provider has already been set");
        }

        this.supplier = () -> pack.addProvider(factory);
    }

    public @NotNull T get() {
        if (Objects.isNull(this.provider)) {
            this.provider = Objects.requireNonNull(this.supplier).get();
        }

        return this.provider;
    }

}
