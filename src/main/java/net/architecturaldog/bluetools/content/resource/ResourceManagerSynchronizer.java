package net.architecturaldog.bluetools.content.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class ResourceManagerSynchronizer {

    public static final @NotNull ResourceManagerSynchronizer INSTANCE = new ResourceManagerSynchronizer();

    private final @NotNull Map<Identifier, ManagerState> states = new Object2ObjectOpenHashMap<>();

    public @Nullable ManagerState getCurrentState(final @NotNull IdentifiableResourceReloadListener listener) {
        return this.getCurrentState(listener.getFabricId());
    }

    public @Nullable ManagerState getCurrentState(final @NotNull Identifier identifier) {
        synchronized (this.states) {
            return this.states.putIfAbsent(identifier, ManagerState.UNSPECIFIED);
        }
    }

    public void setCurrentState(
        final @NotNull IdentifiableResourceReloadListener listener,
        final @NotNull ManagerState state
    )
    {
        this.setCurrentState(listener.getFabricId(), state);
    }

    public void setCurrentState(final @NotNull Identifier identifier, final @NotNull ManagerState state) {
        synchronized (this.states) {
            this.states.put(identifier, state);
        }

        BlueTools.LOGGER.debug("Resource manager '{}' is now {}", identifier, state.name().toLowerCase());
    }

    public @NotNull CompletableFuture<Void> waitForLoader(
        final @NotNull IdentifiableResourceReloadListener listener,
        final @NotNull ManagerState state,
        final @NotNull Executor executor
    )
    {
        return this.waitForLoader(listener.getFabricId(), state, executor);
    }

    public @NotNull CompletableFuture<Void> waitForLoader(
        final @NotNull Identifier identifier,
        final @NotNull ManagerState state,
        final @NotNull Executor executor
    )
    {
        if (!this.states.containsKey(identifier)) {
            return CompletableFuture.completedFuture(null);
        }

        final @NotNull CompletableFuture<Void> future = CompletableFuture.runAsync(
            () -> {
                @Nullable ManagerState current;

                while ((current = this.getCurrentState(identifier)) != null && !current.equals(state)) {
                    Thread.onSpinWait();
                }
            }, executor
        );

        return future.orTimeout(30, TimeUnit.SECONDS);
    }

    public enum ManagerState {

        UNSPECIFIED,
        PREPARING,
        APPLYING,
        VERIFYING,
        FINISHED,

    }

}
