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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public record MiningLevel(@NotNull Color color, @NotNull List<Rule> rules) {

    public static final @NotNull MapCodec<MiningLevel> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Color.CODEC.fieldOf("color").forGetter(MiningLevel::color),
        Rule.CODEC.codec().listOf().fieldOf("rules").forGetter(MiningLevel::rules)
    ).apply(instance, MiningLevel::new));

    public boolean canMine(final @NotNull Block block) {
        return this.canMine(block.getDefaultState());
    }

    public boolean canMine(final @NotNull BlockView blockView, final @NotNull BlockPos blockPos) {
        return this.canMine(blockView.getBlockState(blockPos));
    }

    public boolean canMine(final @NotNull BlockState state) {
        return this.rules().stream().anyMatch(rule -> rule.stateMatches(state));
    }

    public sealed interface Rule permits BlockRule, TagRule {

        @NotNull MapCodec<Rule> CODEC = Codec.mapEither(BlockRule.CODEC, TagRule.CODEC).xmap(
            either -> either.map(Function.identity(), Function.identity()),
            rule -> rule instanceof BlockRule ? Either.left((BlockRule) rule) : Either.right((TagRule) rule)
        );

        boolean inverted();

        boolean stateMatches(final @NotNull BlockState state);

    }

    public record BlockRule(@NotNull Block block, boolean inverted) implements Rule {

        public static final @NotNull MapCodec<BlockRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("block").forGetter(BlockRule::block),
            Codec.BOOL.fieldOf("inverted").forGetter(BlockRule::inverted)
        ).apply(instance, BlockRule::new));

        @Override
        public boolean stateMatches(final @NotNull BlockState state) {
            if (this.inverted()) {
                return !state.isOf(this.block());
            } else {
                return state.isOf(this.block());
            }
        }

    }

    public record TagRule(@NotNull TagKey<Block> tagKey, boolean inverted) implements Rule {

        public static final @NotNull MapCodec<TagRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(RegistryKeys.BLOCK).fieldOf("tag").forGetter(TagRule::tagKey),
            Codec.BOOL.fieldOf("inverted").forGetter(TagRule::inverted)
        ).apply(instance, TagRule::new));

        @Override
        public boolean stateMatches(final @NotNull BlockState state) {
            if (this.inverted()) {
                return !state.isIn(this.tagKey());
            } else {
                return state.isIn(this.tagKey());
            }
        }

    }

}
