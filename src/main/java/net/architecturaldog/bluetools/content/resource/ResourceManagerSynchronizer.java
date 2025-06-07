package net.architecturaldog.bluetools.content.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class ResourceManagerSynchronizer {

    public static final ResourceManagerSynchronizer INSTANCE = new ResourceManagerSynchronizer();

    private final Map<Identifier, ManagerState> states = new Object2ObjectOpenHashMap<>();

    public ManagerState getCurrentState(final IdentifiableResourceReloadListener listener) {
        return this.getCurrentState(listener.getFabricId());
    }

    public ManagerState getCurrentState(final Identifier identifier) {
        synchronized (this.states) {
            return this.states.putIfAbsent(identifier, ManagerState.UNSPECIFIED);
        }
    }

    public void setCurrentState(final IdentifiableResourceReloadListener listener, final ManagerState state) {
        this.setCurrentState(listener.getFabricId(), state);
    }

    public void setCurrentState(final Identifier identifier, final ManagerState state) {
        synchronized (this.states) {
            this.states.put(identifier, state);
        }

        BlueTools.LOGGER.debug("Resource manager '{}' is now {}", identifier, state.name().toLowerCase());
    }

    public CompletableFuture<Void> waitForLoader(
        final IdentifiableResourceReloadListener listener,
        final ManagerState state,
        final Executor executor
    )
    {
        return this.waitForLoader(listener.getFabricId(), state, executor);
    }

    public CompletableFuture<Void> waitForLoader(
        final Identifier identifier,
        final ManagerState state,
        final Executor executor
    )
    {
        if (!this.states.containsKey(identifier)) {
            return CompletableFuture.completedFuture(null);
        }

        final CompletableFuture<Void> future = CompletableFuture.runAsync(
            () -> {
                while (!this.getCurrentState(identifier).equals(state)) Thread.onSpinWait();
            }, executor
        );

        return future.orTimeout(30, TimeUnit.SECONDS);
    }

    public enum ManagerState {

        UNSPECIFIED,
        PREPARING,
        APPLYING,
        FINISHED,

    }

}
