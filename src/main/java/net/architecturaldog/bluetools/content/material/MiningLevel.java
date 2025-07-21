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

        MapCodec<Rule> CODEC = Codec.mapEither(BlockRule.CODEC, TagRule.CODEC).xmap(
            either -> either.map(Function.identity(), Function.identity()),
            rule -> rule instanceof BlockRule ? Either.left((BlockRule) rule) : Either.right((TagRule) rule)
        );

        boolean stateMatches(final BlockState state);

        boolean inverted();

    }

    public record BlockRule(Block block, boolean inverted) implements Rule {

        public static final MapCodec<BlockRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockRule::block),
            Codec.BOOL.fieldOf("inverted").forGetter(BlockRule::inverted)
        ).apply(instance, BlockRule::new));

        @Override
        public boolean stateMatches(final BlockState state) {
            if (this.inverted()) {
                return !state.isOf(this.block());
            } else {
                return state.isOf(this.block());
            }
        }

    }

    public record TagRule(TagKey<Block> tagKey, boolean inverted) implements Rule {

        public static final MapCodec<TagRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(RegistryKeys.BLOCK).fieldOf("tag").forGetter(TagRule::tagKey),
            Codec.BOOL.fieldOf("inverted").forGetter(TagRule::inverted)
        ).apply(instance, TagRule::new));

        @Override
        public boolean stateMatches(final BlockState state) {
            if (this.inverted()) {
                return !state.isIn(this.tagKey());
            } else {
                return state.isIn(this.tagKey());
            }
        }

    }

}
