package net.architecturaldog.bluetools.content.tool;

import com.mojang.serialization.MapCodec;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsToolTypes extends AutoLoader {

    public static final AutoLoaded<ToolType<SimpleTool>> SIMPLE = BlueToolsToolTypes.create("simple", SimpleTool.CODEC);

    private static <P extends Tool> AutoLoaded<ToolType<P>> create(
        final String path,
        final MapCodec<P> codec
    )
    {
        return BlueToolsToolTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends Tool> AutoLoaded<ToolType<P>> create(
        final Identifier identifier,
        final MapCodec<P> codec
    )
    {
        return new AutoLoaded<ToolType<P>>(identifier, () -> codec).on(CommonLoaded.class, self -> {
            Registry.register(BlueToolsRegistries.TOOL_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("tool_types");
    }

}
