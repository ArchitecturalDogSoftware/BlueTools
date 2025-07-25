package net.architecturaldog.bluetools.content.component;

import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.RegistryLoad;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public final class BlueToolsComponentTypes extends AutoLoader {

    public static final @NotNull RegistryLoad<ComponentType<MaterialComponent>> MATERIAL =
        BlueToolsComponentTypes.create("material", builder -> builder.codec(MaterialComponent.CODEC).cache());

    private static <T> @NotNull RegistryLoad<ComponentType<T>> create(
        final @NotNull String path,
        final @NotNull UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        return BlueToolsComponentTypes.create(BlueTools.id(path), function);
    }

    private static <T> @NotNull RegistryLoad<ComponentType<T>> create(
        final @NotNull Identifier identifier,
        final @NotNull UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        final @NotNull ComponentType<T> component = function.apply(ComponentType.builder()).build();

        return new RegistryLoad<>(identifier, Registries.DATA_COMPONENT_TYPE, component);
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("component_types");
    }

}
