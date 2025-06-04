package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public record MaterialTemperature(double kelvin) {

    public static final Codec<MaterialTemperature> CODEC = Codec
        .doubleRange(0.0D, Double.MAX_VALUE)
        .xmap(MaterialTemperature::ofKelvin, MaterialTemperature::kelvin);

    public static final MaterialTemperature WATER = new MaterialTemperature(FluidConstants.WATER_TEMPERATURE);
    public static final MaterialTemperature LAVA = new MaterialTemperature(FluidConstants.LAVA_TEMPERATURE);

    public MaterialTemperature {
        assert this.kelvin() >= 0.0D : "Material temperature must be non-negative";
    }

    public static MaterialTemperature ofKelvin(final double kelvin) {
        return new MaterialTemperature(kelvin);
    }

    public static MaterialTemperature ofCelsius(final double celsius) {
        return MaterialTemperature.ofKelvin(celsius + 273.15D);
    }

    public static MaterialTemperature ofFahrenheit(final double fahrenheit) {
        return MaterialTemperature.ofCelsius((fahrenheit - 32.0D) / 1.8D);
    }

    public double celsius() {
        return this.kelvin() - 273.15D;
    }

    public double fahrenheit() {
        return (this.celsius() * 1.8D) + 32.0D;
    }

}
