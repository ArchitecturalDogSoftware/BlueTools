package net.architecturaldog.bluetools.content.item.tint;

import com.mojang.serialization.MapCodec;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.ClientLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class BlueToolsTintSources extends AutoLoader {

    public static final TintSourceLoader<MaterialTintSource> MATERIAL =
        new TintSourceLoader<>("material", MaterialTintSource.CODEC);

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("tint_sources");
    }

    @Environment(EnvType.CLIENT)
    public static final class TintSourceLoader<T extends TintSource> implements ClientLoaded {

        private final String path;
        private final MapCodec<T> codec;

        public TintSourceLoader(final String path, final MapCodec<T> codec) {
            this.path = path;
            this.codec = codec;
        }

        @Override
        public Identifier getLoaderId() {
            return BlueTools.id(this.path);
        }

        @Override
        public void loadClient() {
            TintSourceTypes.ID_MAPPER.put(this.getLoaderId(), this.codec);
        }

    }

}
