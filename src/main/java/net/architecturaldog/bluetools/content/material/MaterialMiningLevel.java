package net.architecturaldog.bluetools.content.material;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.jaxydog.lodestone.api.CommonLoaded;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record MaterialMiningLevel(List<Rule> rules) {

    public static final Codec<MaterialMiningLevel> CODEC = Rule.CODEC
        .codec()
        .listOf()
        .xmap(MaterialMiningLevel::new, MaterialMiningLevel::rules);

    public MaterialMiningLevel(final List<Rule> rules) {
        this.rules = ImmutableList.copyOf(rules);
    }

    public List<Rule> getRulesFor(final Block block) {
        final RegistryEntry<Block> entry = Registries.BLOCK.getEntry(block);

        return this.rules().stream().filter(rule -> rule.block.map(block::equals, entry::isIn)).toList();
    }

    public record Rule(
        Either<Block, TagKey<Block>> block,
        Optional<Float> speedMultiplier,
        Optional<Boolean> dropItems
    )
    {

        public static final MapCodec<Rule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                Codec
                    .mapEither(
                        Registries.BLOCK.getCodec().fieldOf("id"),
                        TagKey.codec(RegistryKeys.BLOCK).fieldOf("tag")
                    )
                    .forGetter(Rule::block),
                Codec
                    .floatRange(0.0F, Float.MAX_VALUE)
                    .optionalFieldOf("speed_multiplier")
                    .forGetter(Rule::speedMultiplier),
                Codec.BOOL.optionalFieldOf("drop_items").forGetter(Rule::dropItems)
            )
            .apply(instance, Rule::new));

    }

    public static final class Manager extends JsonDataLoader<MaterialMiningLevel>
        implements CommonLoaded, IdentifiableResourceReloadListener
    {

        private Map<RegistryKey<MaterialMiningLevel>, Entry> miningLevels = Map.of();

        public Manager() {
            super(
                MaterialMiningLevel.CODEC,
                new ResourceFinder(BlueToolsRegistries.Keys.MATERIAL_MINING_LEVEL.getValue().getPath(), ".json")
            );
        }

        public Codec<MaterialMiningLevel> getCodec() {
            return Identifier.CODEC.xmap(
                identifier -> this
                    .get(RegistryKey.of(BlueToolsRegistries.Keys.MATERIAL_MINING_LEVEL, identifier))
                    .orElseThrow()
                    .value(),
                level -> this.getId(level).orElseThrow()
            );
        }

        public Optional<Entry> get(final RegistryKey<MaterialMiningLevel> registryKey) {
            return Optional.ofNullable(this.miningLevels.get(registryKey));
        }

        public Optional<Identifier> getId(final MaterialMiningLevel level) {
            return this.miningLevels
                .values()
                .stream()
                .filter(entry -> entry.value().equals(level))
                .map(entry -> entry.key().getValue())
                .findFirst();
        }

        @Override
        public Identifier getLoaderId() {
            return BlueToolsRegistries.Keys.MATERIAL_MINING_LEVEL.getValue();
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
        protected Map<Identifier, MaterialMiningLevel> prepare(
            final ResourceManager resourceManager,
            final Profiler profiler
        )
        {
            return super.prepare(resourceManager, profiler);
        }

        @Override
        protected void apply(
            final Map<Identifier, MaterialMiningLevel> prepared,
            final ResourceManager manager,
            final Profiler profiler
        )
        {
            final Map<RegistryKey<MaterialMiningLevel>, Entry> map = new Object2ObjectOpenHashMap<>(prepared.size());

            for (final Map.Entry<Identifier, MaterialMiningLevel> entry : prepared.entrySet()) {
                final RegistryKey<MaterialMiningLevel> registryKey = RegistryKey.of(
                    BlueToolsRegistries.Keys.MATERIAL_MINING_LEVEL,
                    entry.getKey()
                );

                map.put(registryKey, new Entry(registryKey, entry.getValue()));
            }

            this.miningLevels = ImmutableMap.copyOf(map);

            BlueTools.LOGGER.info("Loaded {} mining levels", this.miningLevels.size());
        }

        public record Entry(RegistryKey<MaterialMiningLevel> key, MaterialMiningLevel value) {

        }

    }

}
