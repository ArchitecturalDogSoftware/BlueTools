package net.architecturaldog.bluetools.content.material;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.utility.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;
import java.util.function.Function;

public record MiningLevel(Color color, List<Rule> rules) {

    public static final MapCodec<MiningLevel> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Color.CODEC.fieldOf("color").forGetter(MiningLevel::color),
        Rule.CODEC.codec().listOf().fieldOf("rules").forGetter(MiningLevel::rules)
    ).apply(instance, MiningLevel::new));

    public boolean canMine(final Block block) {
        return this.canMine(block.getDefaultState());
    }

    public boolean canMine(final BlockView blockView, final BlockPos blockPos) {
        return this.canMine(blockView.getBlockState(blockPos));
    }

    public boolean canMine(final BlockState state) {
        return this.rules().stream().anyMatch(rule -> rule.stateMatches(state));
    }

    public sealed interface Rule permits BlockRule, TagRule {

        MapCodec<Rule> CODEC = Codec.mapEither(BlockRule.CODEC.fieldOf("block"), TagRule.CODEC.fieldOf("tag")).xmap(
            either -> either.map(Function.identity(), Function.identity()),
            rule -> rule instanceof BlockRule ? Either.left((BlockRule) rule) : Either.right((TagRule) rule)
        );

        boolean stateMatches(final BlockState state);

    }

    public record BlockRule(Block block) implements Rule {

        public static final Codec<BlockRule> CODEC = Registries.BLOCK.getCodec().xmap(BlockRule::new, BlockRule::block);

        @Override
        public boolean stateMatches(final BlockState state) {
            return state.isOf(this.block());
        }

    }

    public record TagRule(TagKey<Block> tagKey) implements Rule {

        public static final Codec<TagRule> CODEC = TagKey.codec(RegistryKeys.BLOCK).xmap(TagRule::new, TagRule::tagKey);

        @Override
        public boolean stateMatches(final BlockState state) {
            return state.isIn(this.tagKey());
        }

    }

}
