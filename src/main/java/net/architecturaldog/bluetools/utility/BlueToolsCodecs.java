package net.architecturaldog.bluetools.utility;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@ApiStatus.NonExtendable
public interface BlueToolsCodecs {

    Codec<Byte> NON_NEGATIVE_BYTE = BlueToolsCodecs.range(Codec.BYTE, (byte) 0, Byte.MAX_VALUE);
    Codec<Byte> POSITIVE_BYTE = BlueToolsCodecs.range(Codec.BYTE, (byte) 1, Byte.MAX_VALUE);
    Codec<Short> NON_NEGATIVE_SHORT = BlueToolsCodecs.range(Codec.SHORT, (short) 0, Short.MAX_VALUE);
    Codec<Short> POSITIVE_SHORT = BlueToolsCodecs.range(Codec.SHORT, (short) 1, Short.MAX_VALUE);
    Codec<Long> NON_NEGATIVE_LONG = BlueToolsCodecs.range(Codec.LONG, 0L, Long.MAX_VALUE);
    Codec<Long> POSITIVE_LONG = BlueToolsCodecs.range(Codec.LONG, 1L, Long.MAX_VALUE);
    Codec<Double> NON_NEGATIVE_DOUBLE = BlueToolsCodecs.range(Codec.DOUBLE, 0.0D, Double.MAX_VALUE);
    Codec<Double> POSITIVE_DOUBLE = BlueToolsCodecs.range(Codec.DOUBLE, 1.0D, Double.MAX_VALUE);

    static <N extends Number & Comparable<N>> Codec<N> range(
        final Codec<N> codec,
        final N minInclusive,
        final N maxInclusive
    )
    {
        final Function<N, DataResult<N>> checker = Codec.checkRange(minInclusive, maxInclusive);

        return codec.flatXmap(checker, checker);
    }

    static Codec<Byte> scale(final Codec<Byte> codec, final byte amount) {
        return BlueToolsCodecs
            .scale(
                codec,
                amount,
                Byte.MIN_VALUE,
                Byte.MAX_VALUE,
                (a, b) -> (byte) (a * b),
                (a, b) -> (byte) (a / b),
                (a, b) -> (byte) (a % b)
            );
    }

    static Codec<Short> scale(final Codec<Short> codec, final short amount) {
        return BlueToolsCodecs
            .scale(
                codec,
                amount,
                Short.MIN_VALUE,
                Short.MAX_VALUE,
                (a, b) -> (short) (a * b),
                (a, b) -> (short) (a / b),
                (a, b) -> (short) (a % b)
            );
    }

    static Codec<Integer> scale(final Codec<Integer> codec, final int amount) {
        return BlueToolsCodecs
            .scale(
                codec,
                amount,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                (a, b) -> a * b,
                (a, b) -> a / b,
                (a, b) -> a % b
            );
    }

    static Codec<Long> scale(final Codec<Long> codec, final long amount) {
        return BlueToolsCodecs
            .scale(
                codec,
                amount,
                Long.MIN_VALUE,
                Long.MAX_VALUE,
                (a, b) -> a * b,
                (a, b) -> a / b,
                (a, b) -> a % b
            );
    }

    static Codec<Float> scale(final Codec<Float> codec, final float amount) {
        return BlueToolsCodecs
            .scale(
                codec,
                amount,
                -Float.MAX_VALUE,
                Float.MAX_VALUE,
                (a, b) -> a * b,
                (a, b) -> a / b,
                (a, b) -> a % b
            );
    }

    static Codec<Double> scale(final Codec<Double> codec, final double amount) {
        return BlueToolsCodecs
            .scale(
                codec,
                amount,
                -Double.MAX_VALUE,
                Double.MAX_VALUE,
                (a, b) -> a * b,
                (a, b) -> a / b,
                (a, b) -> a % b
            );
    }

    static <N extends Number> Codec<N> scale(
        final Codec<N> codec,
        final N amount,
        final N minimumValue,
        final N maximumValue,
        final BinaryOperator<N> multiply,
        final BinaryOperator<N> divide,
        final BinaryOperator<N> modulo
    )
    {
        return codec
            .flatXmap(
                number ->
                {
                    final N total = multiply.apply(number, amount);

                    if (total.doubleValue() < minimumValue.doubleValue()) {
                        return DataResult.error(() -> "Min value exceeded (%s < %s)".formatted(total, minimumValue));
                    } else if (total.doubleValue() > maximumValue.doubleValue()) {
                        return DataResult.error(() -> "Max value exceeded (%s > %s)".formatted(total, maximumValue));
                    } else {
                        return DataResult.success(total);
                    }
                },
                number ->
                {
                    if (number.doubleValue() < minimumValue.doubleValue()) {
                        return DataResult.error(() -> "Min value exceeded (%s < %s)".formatted(number, minimumValue));
                    } else if (number.doubleValue() > maximumValue.doubleValue()) {
                        return DataResult.error(() -> "Max value exceeded (%s > %s)".formatted(number, maximumValue));
                    } else if (modulo.apply(number, amount).doubleValue() != 0.0D) {
                        return DataResult.error(() -> "Value not divisible by %s".formatted(amount));
                    } else {
                        return DataResult.success(divide.apply(number, amount));
                    }
                }
            );
    }

    static <T> MapCodec<T> oneOf(final Map<String, Codec<T>> codecs, final String defaultField) {
        if (!codecs.containsKey(defaultField)) {
            throw new IllegalArgumentException("Missing field " + defaultField + "in codec");
        }

        return new MapCodec<>() {

            @Override
            public <U> Stream<U> keys(final DynamicOps<U> ops) {
                return codecs.keySet().stream().map(ops::createString);
            }

            @Override
            public <U> DataResult<T> decode(
                final DynamicOps<U> ops,
                final MapLike<U> input
            )
            {
                final Map<String, U> valueMap = new Object2ObjectOpenHashMap<>();

                for (final Map.Entry<String, Codec<T>> entry : codecs.entrySet()) {
                    final String key = entry.getKey();
                    final @Nullable U value = input.get(key);

                    if (Objects.nonNull(value)) valueMap.put(key, value);
                }

                if (valueMap.isEmpty()) {
                    return DataResult.error(() -> "No valid key in " + input);
                } else if (valueMap.size() > 1) {
                    return DataResult.error(() -> "More than one valid key in " + input);
                } else {
                    final Map.Entry<String, U> entry = valueMap.entrySet().stream().findAny().get();

                    return codecs.get(entry.getKey()).parse(ops, entry.getValue());
                }
            }

            @Override
            public <U> RecordBuilder<U> encode(
                final T input,
                final DynamicOps<U> ops,
                final RecordBuilder<U> prefix
            )
            {
                return prefix.add(defaultField, codecs.get(defaultField).encodeStart(ops, input));
            }

        };
    }

    static <T> AlternativeCodecBuilder<T> chain(final Codec<? extends T> codec) {
        return new AlternativeCodecBuilder<>(codec);
    }

    static <T> AlternativeCodecBuilder<T> chain(final MapCodec<? extends T> codec) {
        return new AlternativeCodecBuilder<>(codec);
    }

    class AlternativeCodecBuilder<T> {

        private final List<Codec<? extends T>> codecs = new ObjectArrayList<>();

        public AlternativeCodecBuilder(final Codec<? extends T> codec) {
            this.codecs.add(codec);
        }

        public AlternativeCodecBuilder(final MapCodec<? extends T> mapCodec) {
            this(mapCodec.codec());
        }

        public AlternativeCodecBuilder<T> chain(final Codec<? extends T> codec) {
            this.codecs.add(codec);

            return this;
        }

        public AlternativeCodecBuilder<T> chain(final MapCodec<? extends T> mapCodec) {
            return this.chain(mapCodec.codec());
        }

        @SuppressWarnings("unchecked")
        public @Nullable Codec<T> build() {
            Codec<T> finalCodec = null;

            for (final Codec<? extends T> codec : this.codecs) {
                if (Objects.isNull(finalCodec)) {
                    finalCodec = (Codec<T>) codec;
                } else {
                    finalCodec = Codec.withAlternative(finalCodec, codec);
                }
            }

            return finalCodec;
        }

    }

}
