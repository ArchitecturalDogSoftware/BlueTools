package net.architecturaldog.bluetools.utility;

import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegistryLoaded<T> implements CommonLoaded {

    private final String path;
    private final Registry<? super T> registry;
    private final T value;

    public RegistryLoaded(final String path, final Registry<? super T> registry, final T value) {
        this.path = path;
        this.registry = registry;
        this.value = value;
    }

    public final T getValue() {
        return this.value;
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

    @Override
    public void loadCommon() {
        Registry.register(this.registry, this.getLoaderId(), this.value);
    }

}
