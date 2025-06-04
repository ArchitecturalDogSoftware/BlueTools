package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public record MaterialValue(long value) {

    public static final Codec<MaterialValue> CODEC = Codec.LONG
        .flatXmap(Codec.checkRange(0L, Long.MAX_VALUE), Codec.checkRange(0L, Long.MAX_VALUE))
        .xmap(MaterialValue::new, MaterialValue::value);

    public static final MaterialValue BUCKET = new MaterialValue(FluidConstants.BUCKET);
    public static final MaterialValue BOTTLE = new MaterialValue(FluidConstants.BOTTLE);
    public static final MaterialValue BOWL = new MaterialValue(FluidConstants.BOWL);
    public static final MaterialValue BLOCK = new MaterialValue(FluidConstants.BLOCK);
    public static final MaterialValue INGOT = new MaterialValue(FluidConstants.INGOT);
    public static final MaterialValue NUGGET = new MaterialValue(FluidConstants.NUGGET);
    public static final MaterialValue DROPLET = new MaterialValue(FluidConstants.DROPLET);

    public MaterialValue {
        assert this.value() >= 0 : "Material value must be non-negative";
    }

    public static MaterialValue ofBuckets(int count) {
        return new MaterialValue(MaterialValue.BUCKET.value() * count);
    }

    public static MaterialValue ofBottles(int count) {
        return new MaterialValue(MaterialValue.BOTTLE.value() * count);
    }

    public static MaterialValue ofBowls(int count) {
        return new MaterialValue(MaterialValue.BOWL.value() * count);
    }

    public static MaterialValue ofBlocks(int count) {
        return new MaterialValue(MaterialValue.BLOCK.value() * count);
    }

    public static MaterialValue ofIngots(int count) {
        return new MaterialValue(MaterialValue.INGOT.value() * count);
    }

    public static MaterialValue ofNuggets(int count) {
        return new MaterialValue(MaterialValue.NUGGET.value() * count);
    }

}
