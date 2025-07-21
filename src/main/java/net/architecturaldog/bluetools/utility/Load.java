package net.architecturaldog.bluetools.utility;

import dev.jaxydog.lodestone.api.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class Load<T> implements CommonLoaded, ClientLoaded, ServerLoaded, DataGenerating {

    private final Identifier identifier;
    private final T value;

    private final Map<Class<? extends Loaded>, List<Runnable>> runnables = new Object2ObjectOpenHashMap<>(4);

    public Load(final Identifier identifier, final T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public Load(final String path, final T value) {
        this(BlueTools.id(path), value);
    }

    public final T getValue() {
        return this.value;
    }

    public Load<T> onCommonLoad(final Runnable runnable) {
        this.runnables.computeIfAbsent(CommonLoaded.class, (key) -> new ObjectArrayList<>()).add(runnable);

        return this;
    }

    public Load<T> onClientLoad(final Runnable runnable) {
        this.runnables.computeIfAbsent(ClientLoaded.class, (key) -> new ObjectArrayList<>()).add(runnable);

        return this;
    }

    public Load<T> onServerLoad(final Runnable runnable) {
        this.runnables.computeIfAbsent(ServerLoaded.class, (key) -> new ObjectArrayList<>()).add(runnable);

        return this;
    }

    public Load<T> onDataGenerate(final Runnable runnable) {
        this.runnables.computeIfAbsent(DataGenerating.class, (key) -> new ObjectArrayList<>()).add(runnable);

        return this;
    }

    @Override
    public final Identifier getLoaderId() {
        return this.identifier;
    }

    @Override
    public void loadCommon() {
        this.runnables.getOrDefault(CommonLoaded.class, List.of()).forEach(Runnable::run);
    }

    @Override
    public void loadClient() {
        this.runnables.getOrDefault(ClientLoaded.class, List.of()).forEach(Runnable::run);
    }

    @Override
    public void loadServer() {
        this.runnables.getOrDefault(ServerLoaded.class, List.of()).forEach(Runnable::run);
    }

    @Override
    public void generate() {
        this.runnables.getOrDefault(DataGenerating.class, List.of()).forEach(Runnable::run);
    }

}
