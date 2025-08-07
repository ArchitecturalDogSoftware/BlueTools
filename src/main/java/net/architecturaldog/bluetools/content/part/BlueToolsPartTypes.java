package net.architecturaldog.bluetools.content.part;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.RegistryLoaded;
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
        return new RegistryLoaded<>(identifier, BlueToolsRegistries.PART_TYPE, () -> codec);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("part_types");
    }

}
