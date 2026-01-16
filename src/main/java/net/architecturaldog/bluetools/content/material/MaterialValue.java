package net.architecturaldog.bluetools.content.material;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.utility.BlueToolsCodecs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.text.Text;

public interface MaterialValue {

    Codec<MaterialValue> CODEC = BlueToolsCodecs
        .<MaterialValue>chain(Droplets.CODEC)
        .chain(Nuggets.CODEC)
        .chain(Ingots.CODEC)
        .chain(Blocks.CODEC)
        .chain(Bowls.CODEC)
        .chain(Bottles.CODEC)
        .chain(Buckets.CODEC)
        .chain(Combined.CODEC)
        .build();
    Codec<MaterialValue> POSITIVE_CODEC = MaterialValue.CODEC.validate(value -> {
        return value.asDroplets() > 0
            ? DataResult.success(value)
            : DataResult.error(() -> "Value " + value + " must be positive");
    });

    long asDroplets();

    String getTranslationKey();

    Text getText();

    default ConversionResult asNuggets() {
        return ConversionResult.of(this, FluidConstants.NUGGET);
    }

    default ConversionResult asIngots() {
        return ConversionResult.of(this, FluidConstants.INGOT);
    }

    default ConversionResult asBlocks() {
        return ConversionResult.of(this, FluidConstants.BLOCK);
    }

    default ConversionResult asBowls() {
        return ConversionResult.of(this, FluidConstants.BOWL);
    }

    default ConversionResult asBottles() {
        return ConversionResult.of(this, FluidConstants.BOTTLE);
    }

    default ConversionResult asBuckets() {
        return ConversionResult.of(this, FluidConstants.BUCKET);
    }

    record ConversionResult(long amount, Droplets remaining) {

        private static ConversionResult of(MaterialValue value, long scale) {
            return new ConversionResult(value.asDroplets() / scale, new Droplets(value.asDroplets() % scale));
        }

    }

    record Droplets(long amount) implements MaterialValue {

        public static final MapCodec<Droplets> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Droplets::new, Droplets::amount)
            .fieldOf("droplets");

        @Override
        public long asDroplets() {
            return this.amount();
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("droplets").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Nuggets(long amount) implements MaterialValue {

        public static final MapCodec<Nuggets> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Nuggets::new, Nuggets::amount)
            .fieldOf("nuggets");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.NUGGET;
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("nuggets").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Ingots(long amount) implements MaterialValue {

        public static final MapCodec<Ingots> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Ingots::new, Ingots::amount)
            .fieldOf("ingots");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.INGOT;
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("ingots").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Blocks(long amount) implements MaterialValue {

        public static final MapCodec<Blocks> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Blocks::new, Blocks::amount)
            .fieldOf("blocks");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BLOCK;
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("blocks").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Bowls(long amount) implements MaterialValue {

        public static final MapCodec<Bowls> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Bowls::new, Bowls::amount)
            .fieldOf("bowls");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BOWL;
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("bowls").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Bottles(long amount) implements MaterialValue {

        public static final MapCodec<Bottles> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Bottles::new, Bottles::amount)
            .fieldOf("bottles");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BOTTLE;
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("bottles").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Buckets(long amount) implements MaterialValue {

        public static final MapCodec<Buckets> CODEC = BlueToolsCodecs.NON_NEGATIVE_LONG
            .xmap(Buckets::new, Buckets::amount)
            .fieldOf("buckets");

        @Override
        public long asDroplets() {
            return this.amount() * FluidConstants.BUCKET;
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("buckets").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.amount());
        }

    }

    record Combined(
        Optional<Long> droplets,
        Optional<Long> nuggets,
        Optional<Long> ingots,
        Optional<Long> blocks,
        Optional<Long> bowls,
        Optional<Long> bottles,
        Optional<Long> buckets
    ) implements MaterialValue
    {

        public static final MapCodec<Combined> CODEC = RecordCodecBuilder.mapCodec(instance -> {
            return instance
                .group(
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("droplets").forGetter(Combined::droplets),
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("nuggets").forGetter(Combined::nuggets),
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("ingots").forGetter(Combined::ingots),
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("blocks").forGetter(Combined::blocks),
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("bowls").forGetter(Combined::bowls),
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("bottles").forGetter(Combined::bottles),
                    BlueToolsCodecs.NON_NEGATIVE_LONG.optionalFieldOf("buckets").forGetter(Combined::buckets)
                )
                .apply(instance, Combined::new);
        });

        @Override
        public long asDroplets() {
            return this.droplets().orElse(0L) +
                this.nuggets().orElse(0L) +
                this.ingots().orElse(0L) +
                this.blocks().orElse(0L) +
                this.bowls().orElse(0L) +
                this.bottles().orElse(0L) +
                this.buckets().orElse(0L);
        }

        @Override
        public String getTranslationKey() {
            return BlueTools.id("droplets").toTranslationKey("materialValue");
        }

        @Override
        public Text getText() {
            return Text.translatable(this.getTranslationKey(), this.asDroplets());
        }

    }

}
