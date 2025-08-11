package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsToolPropertyTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<ToolPropertyType<PartsProperty>> PARTS =
        BlueToolsToolPropertyTypes.create("parts", PartsProperty.CODEC);

    private static <P extends ToolProperty> @NotNull AutoLoaded<DefaultedToolPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec,
        final @NotNull P defaultProperty
    )
    {
        return BlueToolsToolPropertyTypes.create(BlueTools.id(path), codec, defaultProperty);
    }

    private static <P extends ToolProperty> @NotNull AutoLoaded<DefaultedToolPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec,
        final @NotNull P defaultProperty
    )
    {
        return new AutoLoaded<>(identifier, DefaultedToolPropertyType.of(codec, defaultProperty)).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.TOOL_PROPERTY_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    private static <P extends ToolProperty> @NotNull AutoLoaded<ToolPropertyType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec
    )
    {
        return BlueToolsToolPropertyTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends ToolProperty> @NotNull AutoLoaded<ToolPropertyType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec
    )
    {
        return new AutoLoaded<ToolPropertyType<P>>(identifier, () -> codec).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.TOOL_PROPERTY_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("tool_property_types");
    }

}
