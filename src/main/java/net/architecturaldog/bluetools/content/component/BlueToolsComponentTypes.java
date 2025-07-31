package net.architecturaldog.bluetools.content.component;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public final class BlueToolsComponentTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<ComponentType<MaterialComponent>> MATERIAL =
        BlueToolsComponentTypes.create("material", builder -> builder.codec(MaterialComponent.CODEC).cache());
    public static final @NotNull AutoLoaded<ComponentType<MaterialMapComponent>> MATERIAL_MAP =
        BlueToolsComponentTypes.create("material_map", builder -> builder.codec(MaterialMapComponent.CODEC).cache());

    private static <T> @NotNull AutoLoaded<ComponentType<T>> create(
        final @NotNull String path,
        final @NotNull UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        return BlueToolsComponentTypes.create(BlueTools.id(path), function);
    }

    private static <T> @NotNull AutoLoaded<ComponentType<T>> create(
        final @NotNull Identifier identifier,
        final @NotNull UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        final @NotNull ComponentType<T> component = function.apply(ComponentType.builder()).build();

        return new RegistryLoaded<>(identifier, Registries.DATA_COMPONENT_TYPE, component);
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("component_types");
    }

}
