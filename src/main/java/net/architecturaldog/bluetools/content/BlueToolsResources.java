package net.architecturaldog.bluetools.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.jaxydog.lodestone.api.AutoLoader;
import dev.jaxydog.lodestone.api.CommonLoaded;
import dev.jaxydog.lodestone.api.LoadingPriority;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Optional;

public final class BlueToolsResources extends AutoLoader {

    @LoadingPriority(Integer.MIN_VALUE)
    public static final DataManager<Material> MATERIAL =
        new DataManager<>(BlueToolsRegistries.Keys.MATERIAL, Material.CODEC);

    public static final DataManager<MaterialMiningLevel> MATERIAL_MINING_LEVEL =
        new DataManager<>(BlueToolsRegistries.Keys.MATERIAL_MINING_LEVEL, MaterialMiningLevel.CODEC);

    @Override
    public Identifier getLoaderId() {
        return BlueTools.id("resources");
    }

    public static class DataManager<T> extends JsonDataLoader<T>
        implements CommonLoaded, IdentifiableResourceReloadListener
    {

        private final RegistryKey<Registry<T>> registryKey;
        private Map<RegistryKey<T>, Entry<T>> loadedValues = Map.of();

        public DataManager(final RegistryKey<Registry<T>> registryKey, final Codec<T> codec) {
            super(codec, new ResourceFinder(registryKey.getValue().getPath(), ".json"));

            this.registryKey = registryKey;
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
        public void loadCommon() {
            ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
        }

        @Override
        protected void apply(
            final Map<Identifier, T> prepared,
            final ResourceManager manager,
            final Profiler profiler
        )
        {
            final Map<RegistryKey<T>, Entry<T>> loadedValues = new Object2ObjectOpenHashMap<>(prepared.size());

            for (final Map.Entry<Identifier, T> entry : prepared.entrySet()) {
                final RegistryKey<T> registryKey = RegistryKey.of(this.registryKey, entry.getKey());

                loadedValues.put(registryKey, new Entry<>(registryKey, entry.getValue()));
            }

            this.loadedValues = loadedValues;

            BlueTools.LOGGER.info("Loaded {} entries for '{}'", this.loadedValues.size(), this.registryKey.getValue());
        }

        public record Entry<T>(RegistryKey<T> key, T value) {}

    }

}
