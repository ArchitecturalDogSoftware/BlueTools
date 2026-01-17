package net.architecturaldog.bluetools.content.component;

import java.util.function.UnaryOperator;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsComponentTypes extends AutoLoader {

    public static final AutoLoaded<ComponentType<MaterialComponent>> MATERIAL = BlueToolsComponentTypes
        .create("material", builder -> builder.codec(MaterialComponent.CODEC).cache());
    public static final AutoLoaded<ComponentType<PartComponent>> PART = BlueToolsComponentTypes
        .create("part", builder -> builder.codec(PartComponent.CODEC).cache());
    public static final AutoLoaded<ComponentType<ToolComponent>> TOOL = BlueToolsComponentTypes
        .create("tool", builder -> builder.codec(ToolComponent.CODEC).cache());
    public static final AutoLoaded<ComponentType<ToolMaterialsComponent>> TOOL_MATERIALS = BlueToolsComponentTypes
        .create("tool_materials", builder -> builder.codec(ToolMaterialsComponent.CODEC).cache());

    private static <T> AutoLoaded<ComponentType<T>> create(
        final String path,
        final UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        return BlueToolsComponentTypes.create(BlueToolsHelper.createIdentifier(path), function);
    }

    private static <T> AutoLoaded<ComponentType<T>> create(
        final Identifier identifier,
        final UnaryOperator<ComponentType.Builder<T>> function
    )
    {
        return BlueToolsComponentTypes.create(identifier, function.apply(ComponentType.builder()).build());
    }

    private static <T> AutoLoaded<ComponentType<T>> create(final Identifier identifier, final ComponentType<T> type) {
        return new AutoLoaded<>(identifier, type).on(CommonLoaded.class, self -> {
            Registry.register(Registries.DATA_COMPONENT_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("component_types");
    }

}
