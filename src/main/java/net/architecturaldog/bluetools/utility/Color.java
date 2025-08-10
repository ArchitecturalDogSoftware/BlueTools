package net.architecturaldog.bluetools.utility;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record Color(int integer) {

    public static final @NotNull Codec<Color> INT_CODEC = Codec.INT.xmap(Color::new, Color::integer);
    public static final @NotNull Codec<Color> TUPLE_CODEC = Codec
        .list(Codecs.UNSIGNED_BYTE, 3, 4)
        .xmap(
            list -> new Color(list.getFirst(), list.get(1), list.get(2), Objects.requireNonNullElse(list.get(3), 0xFF)),
            color -> List.of(color.r(), color.g(), color.b(), color.a())
        );
    public static final @NotNull MapCodec<Color> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.UNSIGNED_BYTE.fieldOf("r").forGetter(Color::r),
        Codecs.UNSIGNED_BYTE.fieldOf("g").forGetter(Color::g),
        Codecs.UNSIGNED_BYTE.fieldOf("b").forGetter(Color::b),
        Codecs.UNSIGNED_BYTE.optionalFieldOf("a", 0xFF).forGetter(Color::a)
    ).apply(instance, Color::new));
    public static final @NotNull Codec<Color> HEX_CODEC = Codec
        .string(7, 9)
        .comapFlatMap(
            string -> {
                long integer;

                try {
                    integer = Long.decode(string);
                } catch (final NumberFormatException exception) {
                    return DataResult.error(() -> "Invalid hex: " + exception);
                }

                if (string.length() == 9) {
                    final long alpha = integer & 0xFF;

                    integer = (integer >>> 8) | (alpha << 24);
                } else {
                    if (integer < 0) {
                        return DataResult.error(() -> "Color cannot be negative");
                    }

                    integer = ColorHelper.fullAlpha((int) integer);
                }

                return DataResult.success(new Color((int) integer));
            },
            color -> color.a() == 0xFF
                ? "#%02X%02X%02X".formatted(color.r(), color.g(), color.b())
                : "#%02X%02X%02X%02X".formatted(color.r(), color.g(), color.b(), color.a())
        );

    public static final @NotNull Codec<Color> CODEC = Codec.withAlternative(
        Codec.withAlternative(Color.INT_CODEC, Color.MAP_CODEC.codec()),
        Codec.withAlternative(Color.TUPLE_CODEC, Color.HEX_CODEC)
    );

    public static final @NotNull PacketCodec<? super RegistryByteBuf, Color> PACKET_CODEC =
        PacketCodecs.INTEGER.xmap(Color::new, Color::integer);

    public Color(int r, int g, int b) {
        this(Color.pack(r, g, b));
    }

    public Color(int r, int g, int b, int a) {
        this(Color.pack(r, g, b, a));
    }

    private static int pack(int r, int g, int b) {
        return Color.pack(r, g, b, 0xFF);
    }

    private static int pack(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    private static int[] unpack(int rgb) {
        return new int[] { (rgb >>> 16) & 0xFF, (rgb >>> 8) & 0xFF, rgb & 0xFF, (rgb >>> 24) & 0xFF };
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

    public int a() {
        return Color.unpack(this.integer())[3];
    }

}
