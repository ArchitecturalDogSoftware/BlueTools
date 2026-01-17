package net.architecturaldog.bluetools.content.part;

import com.mojang.serialization.MapCodec;

import dev.jaxydog.lodestone.api.AutoLoaded;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlueToolsPartTypes extends AutoLoader {

    public static final AutoLoaded<PartType<SimplePart>> SIMPLE = BlueToolsPartTypes.create("simple", SimplePart.CODEC);

    private static <P extends Part> AutoLoaded<PartType<P>> create(final String path, final MapCodec<P> codec) {
        return BlueToolsPartTypes.create(BlueToolsHelper.createIdentifier(path), codec);
    }

    private static <P extends Part> AutoLoaded<PartType<P>> create(
        final Identifier identifier,
        final MapCodec<P> codec
    )
    {
        return new AutoLoaded<PartType<P>>(identifier, () -> codec).on(CommonLoaded.class, self -> {
            Registry.register(BlueToolsRegistries.PART_TYPE, self.getLoaderId(), self.getValue());
        });
    }

    @Override
    public Identifier getLoaderId() {
        return BlueToolsHelper.createIdentifier("part_types");
    }

}
