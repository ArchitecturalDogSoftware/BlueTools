package net.architecturaldog.bluetools.content;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import dev.jaxydog.lodestone.api.IgnoreLoading;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class BlueToolsResources extends AutoLoader {

    // Don't deadlock, don't be stupid.
    @IgnoreLoading({ })
    public static final Synchronizer SYNCHRONIZER = new Synchronizer();

    public static final JsonManager<MaterialMiningLevel> MATERIAL_MINING_LEVEL = new JsonManager<>(
        BlueToolsRegistries.Keys.MATERIAL_MINING_LEVEL,
        MaterialMiningLevel.CODEC.codec()
    );

    public static final JsonManager<Material> MATERIAL = new JsonManager<>(
        BlueToolsRegistries.Keys.MATERIAL,
        Material.CODEC.codec(),
        List.of(MATERIAL_MINING_LEVEL.getFabricId())
    );

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

    public static final class Synchronizer {

        private final Map<Identifier, State> map = new Object2ObjectOpenHashMap<>();

        public State getState(final Identifier identifier) {
            synchronized (this.map) {
                return this.map.computeIfAbsent(identifier, ignored -> State.UNSET);
            }
        }

        public void setState(final Identifier identifier, final State state) {
            synchronized (this.map) {
                this.map.put(identifier, state);
            }

            BlueTools.LOGGER.debug("Manager '{}' is now {}", identifier, state.name().toLowerCase());
        }

        public CompletableFuture<Void> waitForFinished(final Identifier identifier, final Executor executor) {
            return this.waitFor(identifier, State.FINISH, executor);
        }

        public CompletableFuture<Void> waitFor(
            final Identifier identifier,
            final State state,
            final Executor executor
        )
        {
            return CompletableFuture.runAsync(
                () -> {
                    while (!this.getState(identifier).equals(state)) Thread.onSpinWait();
                }, executor
            ).orTimeout(30, TimeUnit.SECONDS);
        }

        public enum State {

            UNSET,
            PREPARE,
            APPLY,
            FINISH

        }

    }

    public static class JsonManager<T> extends SinglePreparationResourceReloader<Map<Identifier, Resource>>
        implements IdentifiableResourceReloadListener, CommonLoaded
    {

        protected final RegistryKey<Registry<T>> registryKey;
        protected final Codec<T> codec;
        protected final List<Identifier> dependencies;
        protected final ResourceFinder resourceFinder;

        protected Map<RegistryKey<T>, Entry<T>> loadedValues = Map.of();

        public JsonManager(final RegistryKey<Registry<T>> registryKey, final Codec<T> codec) {
            this(registryKey, codec, List.of());
        }

        public JsonManager(
            final RegistryKey<Registry<T>> registryKey,
            final Codec<T> codec,
            final List<Identifier> dependencies
        )
        {
            this.registryKey = registryKey;
            this.codec = codec;
            this.dependencies = dependencies;
            this.resourceFinder = new ResourceFinder(this.registryKey.getValue().getPath(), ".json");
        }

        public Codec<T> getCodec() {
            return this.getEntryCodec().flatComapMap(
                Entry::value,
                value -> this.getEntry(value).map(DataResult::success).orElseGet(
                    () -> DataResult.error(() -> "Unregistered value in " + this.registryKey)
                )
            );
        }

        public Codec<Entry<T>> getEntryCodec() {
            return Identifier.CODEC.comapFlatMap(
                identifier -> this.getEntry(identifier).map(DataResult::success).orElseGet(
                    () -> DataResult.error(() -> "Unknown registry key in " + this.registryKey + ": " + identifier)
                ),
                entry -> entry.key().getValue()
            );
        }

        public Optional<Entry<T>> getEntry(final Identifier identifier) {
            return this.getEntry(RegistryKey.of(this.registryKey, identifier));
        }

        public Optional<Entry<T>> getEntry(final RegistryKey<T> key) {
            return Optional.ofNullable(this.loadedValues.get(key));
        }

        public Optional<Entry<T>> getEntry(final T value) {
            return this.loadedValues.values().stream().filter(entry -> entry.value().equals(value)).findFirst();
        }

        public Optional<Identifier> getId(final Entry<T> entry) {
            return this.getId(entry.value());
        }

        public Optional<Identifier> getId(final T value) {
            return this.getEntry(value).map(entry -> entry.key().getValue());
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
        public Collection<Identifier> getFabricDependencies() {
            return List.copyOf(this.dependencies);
        }

        @Override
        public void loadCommon() {
            BlueToolsResources.SYNCHRONIZER.setState(this.getFabricId(), BlueToolsResources.Synchronizer.State.UNSET);

            ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
        }

        @Override
        public CompletableFuture<Void> reload(
            final Synchronizer synchronizer,
            final ResourceManager resourceManager,
            final Executor prepareExecutor,
            final Executor applyExecutor
        )
        {
            return CompletableFuture.supplyAsync(() -> this.prepare(resourceManager, Profilers.get()), prepareExecutor)
                .thenCompose(synchronizer::whenPrepared)
                .thenComposeAsync(value -> CompletableFuture
                    .allOf(this.getFabricDependencies().stream().map(
                        identifier -> BlueToolsResources.SYNCHRONIZER.waitForFinished(identifier, applyExecutor)
                    ).toArray(CompletableFuture[]::new))
                    .thenCompose(nothing -> CompletableFuture.completedStage(value)))
                .thenAcceptAsync(prepared -> this.apply(prepared, resourceManager, Profilers.get()), applyExecutor);
        }

        @Override
        protected Map<Identifier, Resource> prepare(final ResourceManager manager, final Profiler profiler) {
            BlueToolsResources.SYNCHRONIZER.setState(this.getFabricId(), BlueToolsResources.Synchronizer.State.PREPARE);

            return this.resourceFinder.findResources(manager);
        }

        @Override
        protected void apply(
            final Map<Identifier, Resource> prepared,
            final ResourceManager manager,
            final Profiler profiler
        )
        {
            BlueToolsResources.SYNCHRONIZER.setState(this.getFabricId(), BlueToolsResources.Synchronizer.State.APPLY);

            final Map<RegistryKey<T>, Entry<T>> results = new Object2ObjectOpenHashMap<>(prepared.size());

            for (final Map.Entry<Identifier, Resource> entry : prepared.entrySet()) {
                final Identifier entryId = entry.getKey();
                final Identifier resourceId = this.resourceFinder.toResourceId(entryId);
                final Reader reader;

                try {
                    reader = entry.getValue().getReader();
                } catch (final IOException exception) {
                    BlueTools.LOGGER.error("Couldn't open file '{}' from '{}'", resourceId, entryId);

                    continue;
                }

                final DataResult<T> result = this.codec.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader));

                try {
                    result.ifSuccess(value -> {
                        final RegistryKey<T> registryKey = RegistryKey.of(this.registryKey, resourceId);

                        if (results.putIfAbsent(registryKey, new Entry<>(registryKey, value)) != null) {
                            throw new IllegalStateException("Duplicate file ignored with ID " + resourceId);
                        }
                    }).ifError(error -> {
                        BlueTools.LOGGER.error("Couldn't parse file '{}' from '{}': {}", resourceId, entryId, error);
                    });
                } catch (final IllegalArgumentException | JsonParseException exception) {
                    try {
                        reader.close();
                    } catch (final IOException failedClose) {
                        exception.addSuppressed(failedClose);
                    }

                    BlueTools.LOGGER.error("Couldn't parse file '{}' from '{}': {}", resourceId, entryId, exception);
                }
            }

            this.loadedValues = results;

            BlueTools.LOGGER.info("Loaded {} entries for '{}'", this.loadedValues.size(), this.registryKey.getValue());
            BlueToolsResources.SYNCHRONIZER.setState(this.getFabricId(), BlueToolsResources.Synchronizer.State.FINISH);
        }

        public record Entry<T>(RegistryKey<T> key, T value) {}

    }

}
