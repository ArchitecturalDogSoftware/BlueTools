package net.architecturaldog.bluetools.content.resource;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleJsonResourceManager<T> extends JsonResourceManager<T> {

    private final @NotNull String name;

    public SimpleJsonResourceManager(
        final @NotNull String name,
        final @NotNull RegistryKey<Registry<T>> registryKey,
        final @NotNull Codec<T> codec,
        final @NotNull List<Identifier> fabricDependencies
    )
    {
        super(registryKey, codec, fabricDependencies);

        this.name = name;
    }

    public SimpleJsonResourceManager(
        final @NotNull String name,
        final @NotNull RegistryKey<Registry<T>> registryKey,
        final @NotNull Codec<T> codec
    )
    {
        this(name, registryKey, codec, List.of());
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

}
