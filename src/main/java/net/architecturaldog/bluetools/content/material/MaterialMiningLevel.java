package net.architecturaldog.bluetools.content.material;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.Optional;

public abstract class MaterialMiningLevel {

    public abstract List<Rule> getRules();

    public List<Rule> getRulesFor(final Block block) {
        final RegistryEntry<Block> entry = Registries.BLOCK.getEntry(block);

        return this.getRules().stream().filter(rule -> rule.block.map(block::equals, entry::isIn)).toList();
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

}
