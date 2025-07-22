package net.architecturaldog.bluetools.utility;

import dev.jaxydog.lodestone.api.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Load<T> implements CommonLoaded, ClientLoaded, ServerLoaded, DataGenerating {

    private final @NotNull Identifier identifier;
    private final @NotNull T value;

    private final @NotNull Map<Class<? extends Loaded>, List<Consumer<Load<T>>>> consumers =
        new Object2ObjectOpenHashMap<>(4);

    public Load(final @NotNull Identifier identifier, final @NotNull T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public Load(final @NotNull String path, final @NotNull T value) {
        this(BlueTools.id(path), value);
    }

    public final @NotNull T getValue() {
        return this.value;
    }

    public @NotNull Load<T> thenCommon(final @NotNull Consumer<Load<T>> consumer) {
        this.consumers.computeIfAbsent(CommonLoaded.class, key -> new ObjectArrayList<>()).add(consumer);

        return this;
    }

    public @NotNull Load<T> thenClient(final @NotNull Consumer<Load<T>> consumer) {
        this.consumers.computeIfAbsent(ClientLoaded.class, key -> new ObjectArrayList<>()).add(consumer);

        return this;
    }

    public @NotNull Load<T> thenServer(final @NotNull Consumer<Load<T>> consumer) {
        this.consumers.computeIfAbsent(ServerLoaded.class, key -> new ObjectArrayList<>()).add(consumer);

        return this;
    }

    public @NotNull Load<T> thenGenerate(final @NotNull Consumer<Load<T>> consumer) {
        this.consumers.computeIfAbsent(DataGenerating.class, key -> new ObjectArrayList<>()).add(consumer);

        return this;
    }

    @Override
    public final @NotNull Identifier getLoaderId() {
        return this.identifier;
    }

    @Override
    public void loadCommon() {
        for (final @NotNull Consumer<Load<T>> consumer : this.consumers.getOrDefault(CommonLoaded.class, List.of())) {
            consumer.accept(this);
        }
    }

    @Override
    public void loadClient() {
        for (final @NotNull Consumer<Load<T>> consumer : this.consumers.getOrDefault(ClientLoaded.class, List.of())) {
            consumer.accept(this);
        }
    }

    @Override
    public void loadServer() {
        for (final @NotNull Consumer<Load<T>> consumer : this.consumers.getOrDefault(ServerLoaded.class, List.of())) {
            consumer.accept(this);
        }
    }

    @Override
    public void generate() {
        for (final @NotNull Consumer<Load<T>> consumer : this.consumers.getOrDefault(DataGenerating.class, List.of())) {
            consumer.accept(this);
        }
    }

}
