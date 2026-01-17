package net.architecturaldog.bluetools.content.resource;

import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class SimpleJsonResourceManager<T> extends JsonResourceManager<T> {

    private final String name;

    public SimpleJsonResourceManager(
        final String name,
        final RegistryKey<Registry<T>> registryKey,
        final Codec<T> codec,
        final List<Identifier> loaderDependencies
    )
    {
        super(registryKey, codec, loaderDependencies);

        this.name = name;
    }

    public SimpleJsonResourceManager(
        final String name,
        final RegistryKey<Registry<T>> registryKey,
        final Codec<T> codec
    )
    {
        this(name, registryKey, codec, List.of());
    }

    public SimpleJsonResourceManager(
        final RegistryKey<Registry<T>> registryKey,
        final Codec<T> codec,
        final List<Identifier> loaderDependencies
    )
    {
        this(registryKey.getValue().getPath(), registryKey, codec, loaderDependencies);
    }

    public SimpleJsonResourceManager(final RegistryKey<Registry<T>> registryKey, final Codec<T> codec) {
        this(registryKey.getValue().getPath(), registryKey, codec);
    }

    @Override
    public String getName() {
        return this.name;
    }

}
