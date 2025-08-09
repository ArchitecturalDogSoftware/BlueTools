package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.BlueToolsCodecs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public interface MaterialValue {

    @NotNull Codec<MaterialValue> CODEC = Codec.withAlternative(
        Codec.withAlternative(
            Codec.withAlternative(
                Droplets.CODEC.codec().xmap(Function.identity(), v -> (Droplets) v),
                Nuggets.CODEC.codec()
            ),
            Codec.withAlternative(
                Ingots.CODEC.codec().xmap(Function.identity(), v -> (Ingots) v),
                Blocks.CODEC.codec()
            )
        ),
        Codec.withAlternative(
            Codec.withAlternative(
                Bowls.CODEC.codec().xmap(Function.identity(), v -> (Bowls) v),
                Bottles.CODEC.codec()
            ),
            Codec.withAlternative(
                Buckets.CODEC.codec().xmap(Function.identity(), v -> (Buckets) v),
                Combined.CODEC.codec()
            )
        )
    );
    @NotNull Codec<MaterialValue> POSITIVE_CODEC = MaterialValue.CODEC.validate(value -> value.asDroplets() > 0
        ? DataResult.success(value)
        : DataResult.error(() -> "Value " + value + " must be positive"));

    long asDroplets();

    @NotNull String getTranslationKey();

    @NotNull Text getText();

    default @NotNull ConversionResult asNuggets() {
        return ConversionResult.of(this, FluidConstants.NUGGET);
    }

    default @NotNull ConversionResult asIngots() {
        return ConversionResult.of(this, FluidConstants.INGOT);
    }

    default @NotNull ConversionResult asBlocks() {
        return ConversionResult.of(this, FluidConstants.BLOCK);
    }

    default @NotNull ConversionResult asBowls() {
        return ConversionResult.of(this, FluidConstants.BOWL);
    }

    default @NotNull ConversionResult asBottles() {
        return ConversionResult.of(this, FluidConstants.BOTTLE);
    }

    default @NotNull ConversionResult asBuckets() {
        return ConversionResult.of(this, FluidConstants.BUCKET);
    }

    record ConversionResult(long amount, Droplets remaining) {

        private static @NotNull ConversionResult of(MaterialValue value, long scale) {
            return new ConversionResult(value.asDroplets() / scale, new Droplets(value.asDroplets() % scale));
        }

    }

    record Droplets(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Droplets> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Droplets::new, Droplets::amount).fieldOf("droplets");

        @Override
        public long asDroplets() {
            return this.amount();
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("droplets").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Nuggets(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Nuggets> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Nuggets::new, Nuggets::amount).fieldOf("nuggets");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.NUGGET;
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("nuggets").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Ingots(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Ingots> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Ingots::new, Ingots::amount).fieldOf("ingots");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.INGOT;
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("ingots").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Blocks(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Blocks> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Blocks::new, Blocks::amount).fieldOf("blocks");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BLOCK;
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("blocks").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Bowls(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Bowls> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Bowls::new, Bowls::amount).fieldOf("bowls");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BOWL;
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("bowls").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Bottles(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Bottles> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Bottles::new, Bottles::amount).fieldOf("bottles");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BOTTLE;
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("bottles").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Buckets(long amount) implements MaterialValue {

        public static final @NotNull MapCodec<Buckets> CODEC =
            BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(Buckets::new, Buckets::amount).fieldOf("buckets");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BUCKET;
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("buckets").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Combined(
        @NotNull Optional<Long> droplets,
        @NotNull Optional<Long> nuggets,
        @NotNull Optional<Long> ingots,
        @NotNull Optional<Long> blocks,
        @NotNull Optional<Long> bowls,
        @NotNull Optional<Long> bottles,
        @NotNull Optional<Long> buckets
    )
        implements MaterialValue
    {

        public static final @NotNull MapCodec<Combined> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("droplets").forGetter(Combined::droplets),
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("nuggets").forGetter(Combined::nuggets),
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("ingots").forGetter(Combined::ingots),
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("blocks").forGetter(Combined::blocks),
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("bowls").forGetter(Combined::bowls),
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("bottles").forGetter(Combined::bottles),
            BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("buckets").forGetter(Combined::buckets)
        ).apply(instance, Combined::new));

        @Override
        public long asDroplets() {
            return this.droplets().orElse(0L)
                + this.nuggets().orElse(0L)
                + this.ingots().orElse(0L)
                + this.blocks().orElse(0L)
                + this.bowls().orElse(0L)
                + this.bottles().orElse(0L)
                + this.buckets().orElse(0L);
        }

        @Override
        public @NotNull String getTranslationKey() {
            return BlueTools.id("droplets").toTranslationKey("materialValue");
        }

        @Override
        public @NotNull Text getText() {
            return Text.translatable(this.getTranslationKey(), this.asDroplets());
        }

    }

}
