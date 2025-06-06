package net.architecturaldog.bluetools.content.component;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public final class BlueToolsComponentTypes extends AutoLoader {

    public static final RegistryLoaded<ComponentType<MaterialComponent>> MATERIAL = create(
        "material",
        builder -> builder.codec(MaterialComponent.CODEC).cache()
    );

    private static <T> RegistryLoaded<ComponentType<T>> create(
        final String path,
        final UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        final ComponentType<T> component = function.apply(ComponentType.builder()).build();

        return new RegistryLoaded<>(path, Registries.DATA_COMPONENT_TYPE, component);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("component_types");
    }

}
