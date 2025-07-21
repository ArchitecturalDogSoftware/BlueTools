package net.architecturaldog.bluetools.content.resource;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class SimpleJsonResourceManager<T> extends JsonResourceManager<T> {

    private final String name;

    public SimpleJsonResourceManager(
        final String name,
        final RegistryKey<Registry<T>> registryKey,
        final Codec<T> codec,
        final List<Identifier> fabricDependencies
    )
    {
        super(registryKey, codec, fabricDependencies);

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

    @Override
    public String getName() {
        return this.name;
    }

}
