package net.architecturaldog.bluetools.utility;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record Color(int integer) {

    public static final Codec<Color> INT_CODEC = Codec.INT.xmap(Color::new, Color::integer);
    public static final Codec<Color> TUPLE_CODEC = Codec
        .list(Codecs.UNSIGNED_BYTE, 3, 3)
        .xmap(
            list -> new Color(list.getFirst(), list.get(1), list.get(2)),
            color -> List.of(color.r(), color.g(), color.b())
        );
    public static final MapCodec<Color> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.UNSIGNED_BYTE.fieldOf("r").forGetter(Color::r),
        Codecs.UNSIGNED_BYTE.fieldOf("g").forGetter(Color::g),
        Codecs.UNSIGNED_BYTE.fieldOf("b").forGetter(Color::b)
    ).apply(instance, Color::new));
    public static final Codec<Color> HEX_CODEC = Codec
        .string(7, 7)
        .comapFlatMap(
            string -> {
                final int integer;

                try {
                    integer = Integer.decode(string);
                } catch (final NumberFormatException exception) {
                    return DataResult.error(() -> "Invalid hex: " + exception);
                }

                if (integer >= 0) {
                    return DataResult.success(new Color(integer));
                } else {
                    return DataResult.error(() -> "Color cannot be negative");
                }
            },
            color -> "#%02X%02X%02X".formatted(color.r(), color.g(), color.b())
        );

    public static final Codec<Color> CODEC = Codec.withAlternative(
        Codec.withAlternative(Color.INT_CODEC, Color.MAP_CODEC.codec()),
        Codec.withAlternative(Color.TUPLE_CODEC, Color.HEX_CODEC)
    );

    public static final PacketCodec<? super RegistryByteBuf, Color> PACKET_CODEC =
        PacketCodecs.INTEGER.xmap(Color::new, Color::integer);

    public Color(int r, int g, int b) {
        this(Color.pack(r, g, b));
    }

    private static int pack(int r, int g, int b) {
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    private static int[] unpack(int rgb) {
        return new int[] { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
    }

    public int r() {
        return Color.unpack(this.integer())[0];
    }

    public int g() {
        return Color.unpack(this.integer())[1];
    }

    public int b() {
        return Color.unpack(this.integer())[2];
    }

}
