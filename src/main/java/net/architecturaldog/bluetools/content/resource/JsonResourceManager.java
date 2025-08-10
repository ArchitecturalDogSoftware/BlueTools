package net.architecturaldog.bluetools.content.resource;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.jaxydog.lodestone.api.CommonLoaded;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.resource.ResourceManagerSynchronizer.ManagerState;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

public abstract class JsonResourceManager<T> extends SinglePreparationResourceReloader<Map<Identifier, Resource>>
    implements CommonLoaded, IdentifiableResourceReloadListener
{

    private final @NotNull RegistryKey<Registry<T>> registryKey;
    private final @NotNull Codec<T> codec;
    private final @NotNull List<Identifier> fabricDependencies;
    private final @NotNull ResourceFinder resourceFinder;

    private @NotNull Map<RegistryKey<T>, Entry<T>> loadedEntries = Map.of();

    public JsonResourceManager(final @NotNull RegistryKey<Registry<T>> registryKey, final @NotNull Codec<T> codec) {
        this(registryKey, codec, List.of());
    }

    public JsonResourceManager(
        final @NotNull RegistryKey<Registry<T>> registryKey,
        final @NotNull Codec<T> codec,
        final @NotNull List<Identifier> fabricDependencies
    )
    {
        this.registryKey = registryKey;
        this.codec = codec;
        this.fabricDependencies = fabricDependencies;
        this.resourceFinder = new ResourceFinder(this.registryKey.getValue().getPath(), ".json");

        if (this.fabricDependencies.stream().anyMatch(identifier -> identifier.equals(this.getFabricId()))) {
            throw new IllegalArgumentException("Managers must not depend on themselves");
        }
    }

    public abstract @NotNull String getName();

    protected void prepareVerification() {

    }

    protected void cleanupVerification() {

    }

    protected boolean verifyEntry(final @NotNull Entry<T> entry) {
        return true;
    }

    public @NotNull Comparator<Entry<T>> getEntryComparator() {
        return Comparator.comparing(entry -> entry.key().getValue().toString(), String::compareTo);
    }

    public @NotNull List<Entry<T>> getEntries() {
        return List.copyOf(this.loadedEntries.values());
    }

    public @NotNull List<Entry<T>> getSortedEntries() {
        return this.loadedEntries.values().stream().sorted(this.getEntryComparator()).toList();
    }

    public @NotNull Optional<Entry<T>> getEntry(final @NotNull Identifier identifier) {
        return this.getEntry(RegistryKey.of(this.registryKey, identifier));
    }

    public @NotNull Optional<Entry<T>> getEntry(final @NotNull RegistryKey<T> registryKey) {
        return Optional.ofNullable(this.loadedEntries.get(registryKey));
    }

    public @NotNull Optional<Entry<T>> getEntry(final @NotNull T value) {
        return this.loadedEntries.values().stream().filter(entry -> entry.value().equals(value)).findFirst();
    }

    public @NotNull Codec<T> getCodec() {
        return this.getEntryCodec().flatComapMap(
            Entry::value,
            value -> this.getEntry(value).map(DataResult::success).orElseGet(
                () -> DataResult.error(() -> "Unregistered value in %s: %s".formatted(this.registryKey, value))
            )
        );
    }

    public @NotNull Codec<Entry<T>> getEntryCodec() {
        return Identifier.CODEC.comapFlatMap(
            identifier -> this.getEntry(identifier).map(DataResult::success).orElseGet(
                () -> DataResult.error(() -> "Unknown registry key in %s: %s".formatted(this.registryKey, identifier))
            ),
            entry -> entry.key().getValue()
        );
    }

    @Override
    public @NotNull Identifier getLoaderId() {
        return this.registryKey.getValue();
    }

    @Override
    public @NotNull Identifier getFabricId() {
        return this.getLoaderId();
    }

    @Override
    public @NotNull List<Identifier> getFabricDependencies() {
        return List.copyOf(this.fabricDependencies);
    }

    @Override
    public void loadCommon() {
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.UNSPECIFIED);

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
    }

    protected @NotNull CompletableFuture<Void> waitForFabricDependencies(final @NotNull Executor executor) {
        final @NotNull Stream<CompletableFuture<Void>> futures = this.getFabricDependencies().stream().map(
            id -> ResourceManagerSynchronizer.INSTANCE.waitForLoader(id, ManagerState.FINISHED, executor)
        );

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(
        final @NotNull Synchronizer synchronizer,
        final @NotNull ResourceManager manager,
        final @NotNull Executor prepareExecutor,
        final @NotNull Executor applyExecutor
    )
    {
        return CompletableFuture.supplyAsync(() -> this.prepare(manager, Profilers.get()), prepareExecutor)
            .thenCompose(synchronizer::whenPrepared)
            .thenComposeAsync(value -> this.waitForFabricDependencies(applyExecutor).thenApply(nothing -> value))
            .thenAcceptAsync(prepared -> this.apply(prepared, manager, Profilers.get()), applyExecutor);
    }

    @Override
    protected @NotNull Map<Identifier, Resource> prepare(
        final @NotNull ResourceManager manager,
        final @NotNull Profiler profiler
    )
    {
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.PREPARING);

        return this.resourceFinder.findResources(manager);
    }

    @Override
    protected void apply(
        final @NotNull Map<Identifier, Resource> prepared,
        final @NotNull ResourceManager manager,
        final @NotNull Profiler profiler
    )
    {
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.APPLYING);

        final @NotNull Map<RegistryKey<T>, Entry<T>> loadedEntries = new Object2ObjectOpenHashMap<>(prepared.size());

        for (final @NotNull Map.Entry<Identifier, Resource> resourceEntry : prepared.entrySet()) {
            final @NotNull Identifier entryId = resourceEntry.getKey();
            final @NotNull Identifier resourceId = this.resourceFinder.toResourceId(entryId);
            final @NotNull Reader reader;

            try {
                reader = resourceEntry.getValue().getReader();
            } catch (final @NotNull IOException exception) {
                BlueTools.LOGGER.error("Couldn't open file '{}' from '{}'", resourceEntry, entryId);

                continue;
            }

            try {
                final @NotNull DataResult<T> dataResult = this.codec.parse(
                    JsonOps.INSTANCE,
                    JsonParser.parseReader(reader)
                );

                dataResult.ifSuccess(value -> {
                    final @NotNull RegistryKey<T> registryKey = RegistryKey.of(this.registryKey, resourceId);

                    if (Objects.nonNull(loadedEntries.putIfAbsent(registryKey, new Entry<>(registryKey, value)))) {
                        throw new IllegalStateException("Duplicate file ignored with ID " + resourceId);
                    }
                }).ifError(error -> {
                    BlueTools.LOGGER.error("Couldn't parse file '{}' from '{}': {}", resourceId, entryId, error);
                });
            } catch (final @NotNull IllegalArgumentException | JsonParseException exception) {
                try {
                    reader.close();
                } catch (final @NotNull IOException closeException) {
                    exception.addSuppressed(closeException);
                }

                BlueTools.LOGGER.error("Couldn't parse file '{}' from '{}': {}", resourceId, entryId, exception);
            }
        }

        this.loadedEntries = loadedEntries;

        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.VERIFYING);

        final @NotNull List<RegistryKey<T>> invalidEntries = new ObjectArrayList<>();

        this.prepareVerification();

        for (final Map.Entry<RegistryKey<T>, Entry<T>> mapEntry : this.loadedEntries.entrySet()) {
            if (this.verifyEntry(mapEntry.getValue())) continue;

            invalidEntries.add(mapEntry.getKey());
        }

        invalidEntries.forEach(this.loadedEntries::remove);

        this.cleanupVerification();

        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.FINISHED);

        BlueTools.LOGGER.info("Loaded {} entries for JSON manager '{}'", this.loadedEntries.size(), this.getName());
    }

    public record Entry<T>(RegistryKey<T> key, T value) {}

}
