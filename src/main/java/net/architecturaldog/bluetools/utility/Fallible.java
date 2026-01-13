package net.architecturaldog.bluetools.utility;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNullByDefault;

import com.mojang.serialization.DataResult;

@NotNullByDefault
public sealed interface Fallible<T, E extends Exception> permits Fallible.Okay, Fallible.Error {

    @SuppressWarnings("unchecked")
    static <T, E extends Exception> Fallible<T, E> wrap(final Supplier<T, E> supplier) {
        try {
            return new Fallible.Okay<>(supplier.get());
        } catch (final Exception exception) {
            try {
                return new Fallible.Error<>((E) exception);
            } catch (final ClassCastException ignored) {
                throw new IllegalArgumentException(exception);
            }
        }
    }

    T getOrThrow() throws E;

    Optional<T> getOkay();

    Optional<E> getError();

    void ifOkay(final Consumer<T> consumer);

    void ifError(final Consumer<E> consumer);

    void ifOkayOrElse(final Consumer<T> valueConsumer, final Consumer<E> errorConsumer);

    <O> O map(final Function<T, O> valueFunction, final Function<E, O> exceptionFunction);

    default boolean isOkay() {
        return this instanceof Fallible.Okay;
    }

    default boolean isError() {
        return this instanceof Fallible.Error;
    }

    default DataResult<T> mapToDataResult() {
        return this.map(DataResult::success, error -> DataResult.error(error::toString));
    }

    default DataResult<T> mapToDataResult(final String message) {
        return this.map(DataResult::success, error -> DataResult.error(() -> "%s: %s".formatted(message, error)));
    }

    @FunctionalInterface
    interface Supplier<T, E extends Exception> {

        T get() throws E;

    }

    record Okay<T, E extends Exception>(T value) implements Fallible<T, E> {

        @Override
        public T getOrThrow() {
            return this.value();
        }

        @Override
        public Optional<T> getOkay() {
            return Optional.of(this.value());
        }

        @Override
        public Optional<E> getError() {
            return Optional.empty();
        }

        @Override
        public void ifOkay(final Consumer<T> consumer) {
            consumer.accept(this.value());
        }

        @Override
        public void ifError(final Consumer<E> consumer) {

        }

        @Override
        public void ifOkayOrElse(final Consumer<T> valueConsumer, final Consumer<E> errorConsumer) {
            valueConsumer.accept(this.value());
        }

        @Override
        public <O> O map(final Function<T, O> valueFunction, final Function<E, O> exceptionFunction) {
            return valueFunction.apply(this.value());
        }

    }

    record Error<T, E extends Exception>(E exception) implements Fallible<T, E> {

        @Override
        public T getOrThrow() throws E {
            throw this.exception();
        }

        @Override
        public Optional<T> getOkay() {
            return Optional.empty();
        }

        @Override
        public Optional<E> getError() {
            return Optional.of(this.exception());
        }

        @Override
        public void ifOkay(final Consumer<T> consumer) {

        }

        @Override
        public void ifError(final Consumer<E> consumer) {
            consumer.accept(this.exception());
        }

        @Override
        public void ifOkayOrElse(final Consumer<T> valueConsumer, final Consumer<E> exceptionConsumer) {
            exceptionConsumer.accept(this.exception());
        }

        @Override
        public <O> O map(final Function<T, O> valueFunction, final Function<E, O> exceptionFunction) {
            return exceptionFunction.apply(this.exception());
        }

    }

}
