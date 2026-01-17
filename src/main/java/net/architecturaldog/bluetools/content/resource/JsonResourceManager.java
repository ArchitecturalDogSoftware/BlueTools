package net.architecturaldog.bluetools.content.resource;

import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import dev.jaxydog.lodestone.api.CommonLoaded;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.architecturaldog.bluetools.BlueTools;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public abstract class JsonResourceManager<T> extends SinglePreparationResourceReloader<Map<Identifier, Resource>> implements CommonLoaded {

    private final RegistryKey<Registry<T>> registryKey;
    private final Codec<T> codec;
    private final List<Identifier> loaderDependencies;
    private final ResourceFinder resourceFinder;
    private final String translationPrefix;

    private Map<RegistryKey<T>, Entry<T>> loadedEntries = Map.of();

    public JsonResourceManager(
        final RegistryKey<Registry<T>> registryKey,
        final Codec<T> codec,
        final List<Identifier> loaderDependencies
    )
    {
        this.registryKey = registryKey;
        this.codec = codec;
        this.loaderDependencies = loaderDependencies;
        this.resourceFinder = new ResourceFinder(this.registryKey.getValue().getPath(), ".json");

        if (this.loaderDependencies.stream().anyMatch(identifier -> identifier.equals(this.getLoaderId()))) {
            throw new IllegalArgumentException("Managers must not depend on themselves");
        }

        String translationPrefix = this.registryKey.getValue().getPath();

        while (translationPrefix.matches("_[a-z]")) {
            final int replacementIndex = translationPrefix.indexOf('_') + 1;
            final char replacement = Character.toUpperCase(translationPrefix.charAt(replacementIndex));

            translationPrefix.replaceFirst("_[a-z]", String.valueOf(replacement));
        }

        this.translationPrefix = translationPrefix;
    }

    public abstract String getName();

    protected void prepareVerification() {

    }

    protected void cleanupVerification() {

    }

    protected boolean verifyEntry(final Entry<T> entry) {
        return true;
    }

    public Comparator<Entry<T>> getEntryComparator() {
        return Comparator.comparing(entry -> entry.key().getValue().toString(), String::compareTo);
    }

    public Stream<Entry<T>> streamEntries() {
        return this.loadedEntries.values().stream();
    }

    public Stream<Entry<T>> streamSortedEntries() {
        return this.loadedEntries.values().stream().sorted(this.getEntryComparator());
    }

    public List<Entry<T>> getEntries() {
        return this.streamEntries().toList();
    }

    public List<Entry<T>> getSortedEntries() {
        return this.streamSortedEntries().toList();
    }

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
        return this.getEntryCodec().flatComapMap(Entry::value, value -> {
            return this.getEntry(value).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> "Unregistered value in %s: %s".formatted(this.registryKey, value));
            });
        });
    }

    public Codec<Entry<T>> getEntryCodec() {
        return Identifier.CODEC.comapFlatMap(identifier -> {
            return this.getEntry(identifier).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> "Unknown registry key in %s: %s".formatted(this.registryKey, identifier));
            });
        }, entry -> entry.key().getValue());
    }

    public String getTranslationKey(final Entry<T> entry) {
        return entry.key().getValue().toTranslationKey(this.translationPrefix);
    }

    public String getTranslationKey(final Optional<Entry<T>> entry) {
        return entry.map(this::getTranslationKey).orElseGet(() -> "%s.missing".formatted(this.translationPrefix));
    }

    public MutableText getText(final Entry<T> entry, Object... arguments) {
        return Text.translatable(this.getTranslationKey(entry), arguments);
    }

    public MutableText getText(final Optional<Entry<T>> entry, Object... arguments) {
        return Text.translatable(this.getTranslationKey(entry), arguments);
    }

    @Override
    public Identifier getLoaderId() {
        return this.registryKey.getValue();
    }

    public List<Identifier> getLoaderDependencies() {
        return this.loaderDependencies.stream().distinct().toList();
    }

    @Override
    public void loadCommon() {
        final ResourceLoader loader = ResourceLoader.get(ResourceType.SERVER_DATA);

        loader.registerReloader(this.getLoaderId(), this::reload);

        for (final Identifier identifier : this.getLoaderDependencies()) {
            loader.addReloaderOrdering(identifier, this.getLoaderId());
        }
    }

    @Override
    protected Map<Identifier, Resource> prepare(final ResourceManager manager, final Profiler profiler) {
        return this.resourceFinder.findResources(manager);
    }

    @Override
    protected void apply(
        final Map<Identifier, Resource> prepared,
        final ResourceManager manager,
        final Profiler profiler
    )
    {
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

        final List<RegistryKey<T>> invalidEntries = new ObjectArrayList<>();

        this.prepareVerification();

        for (final Map.Entry<RegistryKey<T>, Entry<T>> mapEntry : this.loadedEntries.entrySet()) {
            if (this.verifyEntry(mapEntry.getValue())) continue;

            invalidEntries.add(mapEntry.getKey());
        }

        invalidEntries.forEach(this.loadedEntries::remove);

        this.cleanupVerification();

        BlueTools.LOGGER.info("Loaded {} entries for JSON manager '{}'", this.loadedEntries.size(), this.getName());
    }

    public record Entry<T>(RegistryKey<T> key, T value) {}

}
