package net.architecturaldog.bluetools.utility;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RegistryLoaded<T> extends AutoLoaded<T> {

    private final @NotNull Registry<? super T> registry;

    public RegistryLoaded(
        final @NotNull String path,
        final @NotNull Registry<? super T> registry,
        final @NotNull T value
    )
    {
        this(BlueTools.id(path), registry, value);
    }

    public RegistryLoaded(
        final @NotNull Identifier identifier,
        final @NotNull Registry<? super T> registry,
        final @NotNull T value
    )
    {
        super(identifier, value);

        this.registry = registry;

        this.on(CommonLoaded.class, self -> Registry.register(this.getRegistry(), this.getLoaderId(), this.getValue()));
    }

    public final @NotNull Registry<? super T> getRegistry() {
        return this.registry;
    }

}
