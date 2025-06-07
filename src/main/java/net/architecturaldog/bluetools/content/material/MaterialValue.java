package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.utility.BlueToolsCodecs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public record MaterialValue(long value) {

    public static final MaterialValue ONE_BUCKET = new MaterialValue(FluidConstants.BUCKET);
    public static final MaterialValue ONE_BOTTLE = new MaterialValue(FluidConstants.BOTTLE);
    public static final MaterialValue ONE_BOWL = new MaterialValue(FluidConstants.BOWL);
    public static final MaterialValue ONE_BLOCK = new MaterialValue(FluidConstants.BLOCK);
    public static final MaterialValue ONE_INGOT = new MaterialValue(FluidConstants.INGOT);
    public static final MaterialValue ONE_NUGGET = new MaterialValue(FluidConstants.NUGGET);
    public static final MaterialValue ONE_DROPLET = new MaterialValue(FluidConstants.DROPLET);

    public static final MapCodec<MaterialValue> CODEC = BlueToolsCodecs
        .oneOf(
            Map.of(
                "droplets",
                BlueToolsCodecs.NON_NEGATIVE_LONG,
                "nuggets",
                BlueToolsCodecs.scale(BlueToolsCodecs.NON_NEGATIVE_LONG, FluidConstants.NUGGET),
                "ingots",
                BlueToolsCodecs.scale(BlueToolsCodecs.NON_NEGATIVE_LONG, FluidConstants.INGOT),
                "blocks",
                BlueToolsCodecs.scale(BlueToolsCodecs.NON_NEGATIVE_LONG, FluidConstants.BLOCK),
                "bowls",
                BlueToolsCodecs.scale(BlueToolsCodecs.NON_NEGATIVE_LONG, FluidConstants.BOWL),
                "bottles",
                BlueToolsCodecs.scale(BlueToolsCodecs.NON_NEGATIVE_LONG, FluidConstants.BOTTLE),
                "buckets",
                BlueToolsCodecs.scale(BlueToolsCodecs.NON_NEGATIVE_LONG, FluidConstants.BUCKET)
            ),
            "droplets"
        )
        .xmap(MaterialValue::new, MaterialValue::value);

    public static MaterialValue droplets(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_DROPLET.value());
    }

    public static MaterialValue nuggets(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_NUGGET.value());
    }

    public static MaterialValue ingots(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_INGOT.value());
    }

    public static MaterialValue blocks(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_BLOCK.value());
    }

    public static MaterialValue bowls(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_BOWL.value());
    }

    public static MaterialValue bottles(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_BOTTLE.value());
    }

    public static MaterialValue buckets(final long value) {
        return new MaterialValue(value * MaterialValue.ONE_BUCKET.value());
    }

    public static Codec<Long> getLongCodec(long baseValue) {
        return BlueToolsCodecs.NON_NEGATIVE_LONG.xmap(v -> v * baseValue, v -> v / baseValue);
    }

    public static Codec<MaterialValue> getCodec(long baseValue) {
        return MaterialValue.getLongCodec(baseValue).xmap(MaterialValue::new, MaterialValue::value);
    }

    public MaterialValue apply(final UnaryOperator<Long> operator) {
        return new MaterialValue(operator.apply(this.value()));
    }

    public MaterialValue neg() {
        return this.apply(value -> -value);
    }

    public MaterialValue combine(final MaterialValue other, final BinaryOperator<Long> operator) {
        return new MaterialValue(operator.apply(this.value(), other.value()));
    }

    public MaterialValue add(final MaterialValue other) {
        return this.combine(other, Long::sum);
    }

    public MaterialValue sub(final MaterialValue other) {
        return this.combine(other, (a, b) -> a - b);
    }

    public MaterialValue mul(final MaterialValue other) {
        return this.combine(other, (a, b) -> a * b);
    }

    public MaterialValue div(final MaterialValue other) {
        return this.combine(other, (a, b) -> a / b);
    }

    public MaterialValue rem(final MaterialValue other) {
        return this.combine(other, (a, b) -> a % b);
    }

    public Counts counts() {
        final long blocks = this.value() / FluidConstants.BLOCK;
        final long ingots = (this.value() - blocks) / FluidConstants.INGOT;
        final long nuggets = (this.value() - blocks - ingots) / FluidConstants.NUGGET;
        final long droplets = this.value() - blocks - ingots - nuggets;

        return new Counts(blocks, ingots, nuggets, droplets);
    }

    public record Counts(long blocks, long ingots, long nuggets, long droplets) {

        public static final MapCodec<Counts> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlueToolsCodecs.NON_NEGATIVE_LONG.fieldOf("blocks").forGetter(Counts::blocks),
            BlueToolsCodecs.NON_NEGATIVE_LONG.fieldOf("ingots").forGetter(Counts::ingots),
            BlueToolsCodecs.NON_NEGATIVE_LONG.fieldOf("nuggets").forGetter(Counts::nuggets),
            BlueToolsCodecs.NON_NEGATIVE_LONG.fieldOf("droplets").forGetter(Counts::droplets)
        ).apply(instance, Counts::new));

        public MaterialValue materialValue() {
            return MaterialValue.blocks(this.blocks())
                .add(MaterialValue.ingots(this.ingots()))
                .add(MaterialValue.nuggets(this.nuggets()))
                .add(MaterialValue.droplets(this.droplets()));
        }

    }

}
