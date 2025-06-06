package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.utility.BlueToolsCodecs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

import java.util.Map;

public record MaterialValue(long value) {

    public static final MaterialValue ONE_BUCKET = new MaterialValue(FluidConstants.BUCKET);
    public static final MaterialValue ONE_BOTTLE = new MaterialValue(FluidConstants.BOTTLE);
    public static final MaterialValue ONE_BOWL = new MaterialValue(FluidConstants.BOWL);
    public static final MaterialValue ONE_BLOCK = new MaterialValue(FluidConstants.BLOCK);
    public static final MaterialValue ONE_INGOT = new MaterialValue(FluidConstants.INGOT);
    public static final MaterialValue ONE_NUGGET = new MaterialValue(FluidConstants.NUGGET);
    public static final MaterialValue ONE_DROPLET = new MaterialValue(FluidConstants.DROPLET);

    private static final Codec<Long> NON_NEGATIVE_LONG =
        Codec.LONG.flatXmap(Codec.checkRange(0L, Long.MAX_VALUE), Codec.checkRange(0L, Long.MAX_VALUE));

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

    public MaterialValue {
        assert this.value() >= 0 : "Material value must be non-negative";
    }

    public static Codec<Long> getLongCodec(long baseValue) {
        return MaterialValue.NON_NEGATIVE_LONG.xmap(v -> v * baseValue, v -> v / baseValue);
    }

    public static Codec<MaterialValue> getCodec(long baseValue) {
        return MaterialValue.getLongCodec(baseValue).xmap(MaterialValue::new, MaterialValue::value);
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
            MaterialValue.NON_NEGATIVE_LONG.fieldOf("blocks").forGetter(Counts::blocks),
            MaterialValue.NON_NEGATIVE_LONG.fieldOf("ingots").forGetter(Counts::ingots),
            MaterialValue.NON_NEGATIVE_LONG.fieldOf("nuggets").forGetter(Counts::nuggets),
            MaterialValue.NON_NEGATIVE_LONG.fieldOf("droplets").forGetter(Counts::droplets)
        ).apply(instance, Counts::new));

        public MaterialValue materialValue() {
            return new MaterialValue(
                (this.blocks() * FluidConstants.BLOCK)
                    + (this.ingots() * FluidConstants.INGOT)
                    + (this.nuggets() * FluidConstants.NUGGET)
                    + this.droplets());
        }

    }

}
