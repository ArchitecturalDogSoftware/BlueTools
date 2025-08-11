package net.architecturaldog.bluetools.content.component;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public final class BlueToolsComponentTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<ComponentType<MaterialComponent>> MATERIAL =
        BlueToolsComponentTypes.create("material", builder -> builder.codec(MaterialComponent.CODEC).cache());
    public static final @NotNull AutoLoaded<ComponentType<PartComponent>> PART =
        BlueToolsComponentTypes.create("part", builder -> builder.codec(PartComponent.CODEC).cache());
    public static final @NotNull AutoLoaded<ComponentType<ToolComponent>> TOOL =
        BlueToolsComponentTypes.create("tool", builder -> builder.codec(ToolComponent.CODEC).cache());
    public static final @NotNull AutoLoaded<ComponentType<ToolMaterialsComponent>> TOOL_MATERIALS =
        BlueToolsComponentTypes.create(
            "tool_materials",
            builder -> builder.codec(ToolMaterialsComponent.CODEC).cache()
        );

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
        return new AutoLoaded<>(identifier, function.apply(ComponentType.builder()).build()).on(
            CommonLoaded.class,
            self -> Registry.register(Registries.DATA_COMPONENT_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("component_types");
    }

}
