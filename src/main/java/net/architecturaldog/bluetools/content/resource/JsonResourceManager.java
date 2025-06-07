package net.architecturaldog.bluetools.content.resource;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.jaxydog.lodestone.api.CommonLoaded;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
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

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

public abstract class JsonResourceManager<T> extends SinglePreparationResourceReloader<Map<Identifier, Resource>>
    implements CommonLoaded, IdentifiableResourceReloadListener
{

    private final RegistryKey<Registry<T>> registryKey;
    private final Codec<T> codec;
    private final List<Identifier> fabricDependencies;
    private final ResourceFinder resourceFinder;

    private Map<RegistryKey<T>, Entry<T>> loadedEntries = Map.of();

    public JsonResourceManager(final RegistryKey<Registry<T>> registryKey, final Codec<T> codec) {
        this(registryKey, codec, List.of());
    }

    public JsonResourceManager(
        final RegistryKey<Registry<T>> registryKey,
        final Codec<T> codec,
        final List<Identifier> fabricDependencies
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

    public abstract String getName();

    public Optional<Entry<T>> getEntry(final Identifier identifier) {
        return this.getEntry(RegistryKey.of(this.registryKey, identifier));
    }

    public Optional<Entry<T>> getEntry(final RegistryKey<T> registryKey) {
        return Optional.ofNullable(this.loadedEntries.get(registryKey));
    }

    public Optional<Entry<T>> getEntry(final T value) {
        return this.loadedEntries.values().stream().filter(entry -> entry.value().equals(value)).findFirst();
    }

    public Codec<T> getCodec() {
        return this.getEntryCodec().flatComapMap(
            Entry::value,
            value -> this.getEntry(value).map(DataResult::success).orElseGet(
                () -> DataResult.error(() -> "Unregistered value in %s: %s".formatted(this.registryKey, value))
            )
        );
    }

    public Codec<Entry<T>> getEntryCodec() {
        return Identifier.CODEC.comapFlatMap(
            identifier -> this.getEntry(identifier).map(DataResult::success).orElseGet(
                () -> DataResult.error(() -> "Unknown registry key in %s: %s".formatted(this.registryKey, identifier))
            ),
            entry -> entry.key().getValue()
        );
    }

    @Override
    public Identifier getLoaderId() {
        return this.registryKey.getValue();
    }

    @Override
    public Identifier getFabricId() {
        return this.getLoaderId();
    }

    @Override
    public List<Identifier> getFabricDependencies() {
        return List.copyOf(this.fabricDependencies);
    }

    @Override
    public void loadCommon() {
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.UNSPECIFIED);

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
    }

    protected CompletableFuture<Void> waitForFabricDependencies(final Executor executor) {
        final Stream<CompletableFuture<Void>> futures = this.getFabricDependencies().stream().map(
            id -> ResourceManagerSynchronizer.INSTANCE.waitForLoader(id, ManagerState.FINISHED, executor)
        );

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<Void> reload(
        final Synchronizer synchronizer,
        final ResourceManager manager,
        final Executor prepareExecutor,
        final Executor applyExecutor
    )
    {
        return CompletableFuture.supplyAsync(() -> this.prepare(manager, Profilers.get()), prepareExecutor)
            .thenCompose(synchronizer::whenPrepared)
            .thenComposeAsync(value -> this.waitForFabricDependencies(applyExecutor).thenApply(nothing -> value))
            .thenAcceptAsync(prepared -> this.apply(prepared, manager, Profilers.get()), applyExecutor);
    }

    @Override
    protected Map<Identifier, Resource> prepare(final ResourceManager manager, final Profiler profiler) {
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.PREPARING);

        return this.resourceFinder.findResources(manager);
    }

    @Override
    protected void apply(
        final Map<Identifier, Resource> prepared,
        final ResourceManager manager,
        final Profiler profiler
    )
    {
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.APPLYING);

        final Map<RegistryKey<T>, Entry<T>> loadedEntries = new Object2ObjectOpenHashMap<>(prepared.size());

        for (final Map.Entry<Identifier, Resource> resourceEntry : prepared.entrySet()) {
            final Identifier entryId = resourceEntry.getKey();
            final Identifier resourceId = this.resourceFinder.toResourceId(entryId);
            final Reader reader;

            try {
                reader = resourceEntry.getValue().getReader();
            } catch (final IOException exception) {
                BlueTools.LOGGER.error("Couldn't open file '{}' from '{}'", resourceEntry, entryId);

                continue;
            }

            try {
                final DataResult<T> dataResult = this.codec.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader));

                dataResult.ifSuccess(value -> {
                    final RegistryKey<T> registryKey = RegistryKey.of(this.registryKey, resourceId);

                    if (Objects.nonNull(loadedEntries.putIfAbsent(registryKey, new Entry<>(registryKey, value)))) {
                        throw new IllegalStateException("Duplicate file ignored with ID " + resourceId);
                    }
                }).ifError(error -> {
                    BlueTools.LOGGER.error("Couldn't parse file '{}' from '{}': {}", resourceId, entryId, error);
                });
            } catch (final IllegalArgumentException | JsonParseException exception) {
                try {
                    reader.close();
                } catch (final IOException closeException) {
                    exception.addSuppressed(closeException);
                }

                BlueTools.LOGGER.error("Couldn't parse file '{}' from '{}': {}", resourceId, entryId, exception);
            }
        }

        this.loadedEntries = loadedEntries;

        BlueTools.LOGGER.info("Loaded {} entries for JSON manager '{}'", this.loadedEntries.size(), this.getName());
        ResourceManagerSynchronizer.INSTANCE.setCurrentState(this, ManagerState.FINISHED);
    }

    public record Entry<T>(RegistryKey<T> key, T value) {}

}
