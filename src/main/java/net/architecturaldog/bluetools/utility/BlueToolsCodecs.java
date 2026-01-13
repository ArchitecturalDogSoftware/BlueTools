package net.architecturaldog.bluetools.utility;

import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface BlueToolsCodecs {

    @NotNull
    Codec<Byte> NON_NEGATIVE_BYTE = BlueToolsCodecs.range(Codec.BYTE, (byte) 0, Byte.MAX_VALUE);
    @NotNull
    Codec<Byte> POSITIVE_BYTE = BlueToolsCodecs.range(Codec.BYTE, (byte) 1, Byte.MAX_VALUE);

    @NotNull
    Codec<Short> NON_NEGATIVE_SHORT = BlueToolsCodecs.range(Codec.SHORT, (short) 0, Short.MAX_VALUE);
    @NotNull
    Codec<Short> POSITIVE_SHORT = BlueToolsCodecs.range(Codec.SHORT, (short) 1, Short.MAX_VALUE);

    @NotNull
    Codec<Long> NON_NEGATIVE_LONG = BlueToolsCodecs.range(Codec.LONG, 0L, Long.MAX_VALUE);
    @NotNull
    Codec<Long> POSITIVE_LONG = BlueToolsCodecs.range(Codec.LONG, 1L, Long.MAX_VALUE);

    @NotNull
    Codec<Double> NON_NEGATIVE_DOUBLE = BlueToolsCodecs.range(Codec.DOUBLE, 0.0D, Double.MAX_VALUE);
    @NotNull
    Codec<Double> POSITIVE_DOUBLE = BlueToolsCodecs.range(Codec.DOUBLE, 1.0D, Double.MAX_VALUE);

    static <N extends Number & Comparable<N>> @NotNull Codec<N> range(
        final @NotNull Codec<N> codec,
        final @NotNull N minInclusive,
        final @NotNull N maxInclusive
    )
    {
        final @NotNull Function<N, DataResult<N>> checker = Codec.checkRange(minInclusive, maxInclusive);

        return codec.flatXmap(checker, checker);
    }

    static @NotNull Codec<Byte> scale(final @NotNull Codec<Byte> codec, final byte amount) {
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

    static @NotNull Codec<Short> scale(final @NotNull Codec<Short> codec, final short amount) {
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

    static @NotNull Codec<Integer> scale(final @NotNull Codec<Integer> codec, final int amount) {
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

    static @NotNull Codec<Long> scale(final @NotNull Codec<Long> codec, final long amount) {
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

    static @NotNull Codec<Float> scale(final @NotNull Codec<Float> codec, final float amount) {
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

    static @NotNull Codec<Double> scale(final @NotNull Codec<Double> codec, final double amount) {
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

    static <N extends Number> @NotNull Codec<N> scale(
        final @NotNull Codec<N> codec,
        final @NotNull N amount,
        final @NotNull N minimumValue,
        final @NotNull N maximumValue,
        final @NotNull BinaryOperator<N> multiply,
        final @NotNull BinaryOperator<N> divide,
        final @NotNull BinaryOperator<N> modulo
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

    static <T> @NotNull MapCodec<T> oneOf(
        final @NotNull Map<String, Codec<T>> codecs,
        final @NotNull String defaultField
    )
    {
        if (!codecs.containsKey(defaultField)) {
            throw new IllegalArgumentException("Missing field " + defaultField + "in codec");
        }

        return new MapCodec<>() {

            @Override
            public <U> @NotNull Stream<U> keys(final @NotNull DynamicOps<U> ops) {
                return codecs.keySet().stream().map(ops::createString);
            }

            @Override
            public <U> @NotNull DataResult<T> decode(
                final @NotNull DynamicOps<U> ops,
                final @NotNull MapLike<U> input
            )
            {
                final @NotNull Map<String, U> valueMap = new Object2ObjectOpenHashMap<>();

                for (final @NotNull Map.Entry<String, Codec<T>> entry : codecs.entrySet()) {
                    final String key = entry.getKey();
                    final @Nullable U value = input.get(key);

                    if (Objects.nonNull(value)) valueMap.put(key, value);
                }

                if (valueMap.isEmpty()) {
                    return DataResult.error(() -> "No valid key in " + input);
                } else if (valueMap.size() > 1) {
                    return DataResult.error(() -> "More than one valid key in " + input);
                } else {
                    final @NotNull Map.Entry<String, U> entry = valueMap.entrySet().stream().findAny().get();

                    return codecs.get(entry.getKey()).parse(ops, entry.getValue());
                }
            }

            @Override
            public <U> @NotNull RecordBuilder<U> encode(
                final @NotNull T input,
                final @NotNull DynamicOps<U> ops,
                final @NotNull RecordBuilder<U> prefix
            )
            {
                return prefix.add(defaultField, codecs.get(defaultField).encodeStart(ops, input));
            }

        };
    }

}
