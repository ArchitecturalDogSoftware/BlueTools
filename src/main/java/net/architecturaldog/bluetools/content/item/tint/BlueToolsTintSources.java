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
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class BlueToolsTintSources extends AutoLoader {

    public static final @NotNull TintSourceLoader<MaterialTintSource> MATERIAL =
        new TintSourceLoader<>("material", MaterialTintSource.CODEC);
    public static final @NotNull TintSourceLoader<ToolMaterialTintSource> TOOL_MATERIAL =
        new TintSourceLoader<>("tool_material", ToolMaterialTintSource.CODEC);

    @Override
    public @NotNull Identifier getLoaderId() {
        return BlueTools.id("tint_sources");
    }

    @Environment(EnvType.CLIENT)
    public static final class TintSourceLoader<T extends TintSource> implements ClientLoaded {

        private final @NotNull String path;
        private final @NotNull MapCodec<T> codec;

        public TintSourceLoader(final @NotNull String path, final @NotNull MapCodec<T> codec) {
            this.path = path;
            this.codec = codec;
        }

        @Override
        public @NotNull Identifier getLoaderId() {
            return BlueTools.id(this.path);
        }

        @Override
        public void loadClient() {
            TintSourceTypes.ID_MAPPER.put(this.getLoaderId(), this.codec);
        }

    }

}
