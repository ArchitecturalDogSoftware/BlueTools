package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.CodecCache;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlueToolsComponentType<T> implements ComponentType<T>, CommonLoaded {

    private static final CodecCache CACHE = new CodecCache(512);

    private final String path;
    private final @Nullable Codec<T> codec;
    private final PacketCodec<? super RegistryByteBuf, T> packetCodec;

    public BlueToolsComponentType(
        final String path,
        final @Nullable Codec<T> codec,
        final PacketCodec<? super RegistryByteBuf, T> packetCodec
    )
    {
        this.path = path;
        this.codec = codec;
        this.packetCodec = packetCodec;
    }

    public static <T> Builder<T> builder(final String path) {
        return new Builder<>(path);
    }

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id(this.path);
    }

    @Override
    public void loadCommon() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, this.getLoaderId(), this);
    }

    @Override
    public @Nullable Codec<T> getCodec() {
        return this.codec;
    }

    @Override
    public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
        return this.packetCodec;
    }

    @Override
    public String toString() {
        return Util.registryValueToString(Registries.DATA_COMPONENT_TYPE, this);
    }

    public static class Builder<T> {

        private final String path;
        private @Nullable Codec<T> codec;
        private @Nullable PacketCodec<? super RegistryByteBuf, T> packetCodec;
        private boolean shouldCacheCodecs = false;

        public Builder(final String path) {
            this.path = path;
        }

        public Builder<T> codec(final Codec<T> codec) {
            this.codec = codec;

            return this;
        }

        public Builder<T> packetCodec(final PacketCodec<? super RegistryByteBuf, T> packetCodec) {
            this.packetCodec = packetCodec;

            return this;
        }

        public Builder<T> cache() {
            this.shouldCacheCodecs = true;

            return this;
        }

        public BlueToolsComponentType<T> build() {
            final Codec<T> codec = this.shouldCacheCodecs ? BlueToolsComponentType.CACHE.wrap(this.codec) : this.codec;
            final PacketCodec<? super RegistryByteBuf, T> packetCodec = Objects.requireNonNullElseGet(
                this.packetCodec,
                () -> PacketCodecs.registryCodec(Objects.requireNonNull(this.codec, "Missing codec for component"))
            );

            return new BlueToolsComponentType<>(path, codec, packetCodec);
        }

    }

}
