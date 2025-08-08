package net.architecturaldog.bluetools.content.part;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class BlueToolsPartTypes extends AutoLoader {

    public static final @NotNull AutoLoaded<PartType<SimplePart>> SIMPLE =
        BlueToolsPartTypes.create("simple", SimplePart.CODEC);

    private static <P extends Part> @NotNull AutoLoaded<PartType<P>> create(
        final @NotNull String path,
        final @NotNull MapCodec<P> codec
    )
    {
        return BlueToolsPartTypes.create(BlueTools.id(path), codec);
    }

    private static <P extends Part> @NotNull AutoLoaded<PartType<P>> create(
        final @NotNull Identifier identifier,
        final @NotNull MapCodec<P> codec
    )
    {
        return new AutoLoaded<PartType<P>>(identifier, () -> codec).on(
            CommonLoaded.class,
            self -> Registry.register(BlueToolsRegistries.PART_TYPE, self.getLoaderId(), self.getValue())
        );
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("part_types");
    }

}
