package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.MapCodec;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsToolPropertyTypes extends AutoLoader {

    public static final AutoLoaded<ToolPropertyType<PartsProperty>> PARTS = BlueToolsToolPropertyTypes
        .create("parts", PartsProperty.CODEC);

    private static <P extends ToolProperty> AutoLoaded<DefaultedToolPropertyType<P>> create(
        final String path,
        final MapCodec<P> codec,
        final P defaultProperty
    )
    {
        return BlueToolsToolPropertyTypes.create(BlueToolsHelper.createIdentifier(path), codec, defaultProperty);
    }

    private static <P extends ToolProperty> AutoLoaded<DefaultedToolPropertyType<P>> create(
        final Identifier identifier,
        final MapCodec<P> codec,
        final P defaultProperty
    )
    {
        final DefaultedToolPropertyType<P> type = DefaultedToolPropertyType.of(codec, defaultProperty);

        return new AutoLoaded<>(identifier, type).on(CommonLoaded.class, self -> {
            Registry.register(BlueToolsRegistries.TOOL_PROPERTY_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    private static <P extends ToolProperty> AutoLoaded<ToolPropertyType<P>> create(
        final String path,
        final MapCodec<P> codec
    )
    {
        return BlueToolsToolPropertyTypes.create(BlueToolsHelper.createIdentifier(path), codec);
    }

    private static <P extends ToolProperty> AutoLoaded<ToolPropertyType<P>> create(
        final Identifier identifier,
        final MapCodec<P> codec
    )
    {
        return new AutoLoaded<ToolPropertyType<P>>(identifier, () -> codec).on(CommonLoaded.class, self -> {
            Registry.register(BlueToolsRegistries.TOOL_PROPERTY_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("tool_property_types");
    }

}
