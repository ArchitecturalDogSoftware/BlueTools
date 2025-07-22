package net.architecturaldog.bluetools.content.material;

import com.mojang.serialization.MapCodec;
import net.architecturaldog.bluetools.utility.BlueToolsCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public sealed interface Temperature permits Temperature.Kelvin, Temperature.Celsius, Temperature.Fahrenheit {

    @NotNull MapCodec<Temperature> CODEC = BlueToolsCodecs.oneOf(
        Map.of(
            "kelvin", BlueToolsCodecs.NON_NEGATIVE_DOUBLE.xmap(Kelvin::new, Temperature::degreesKelvin),
            "celsius", BlueToolsCodecs.NON_NEGATIVE_DOUBLE.xmap(Celsius::new, Temperature::degreesCelsius),
            "fahrenheit", BlueToolsCodecs.NON_NEGATIVE_DOUBLE.xmap(Fahrenheit::new, Temperature::degreesFahrenheit)
        ),
        "kelvin"
    );

    double degreesKelvin();

    double degreesCelsius();

    double degreesFahrenheit();

    record Kelvin(double degrees) implements Temperature {

        public static final double ZERO_CELSIUS = 273.15D;

        @Override
        public double degreesKelvin() {
            return this.degrees();
        }

        @Override
        public double degreesCelsius() {
            return this.degrees() + Kelvin.ZERO_CELSIUS;
        }

        @Override
        public double degreesFahrenheit() {
            return Celsius.toFahrenheit(this.degreesCelsius());
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof final Temperature temperature) {
                return this.degreesKelvin() == temperature.degreesKelvin();
            }

            return false;
        }

    }

    record Celsius(double degrees) implements Temperature {

        public static double toFahrenheit(final double celsius) {
            return (celsius * 1.8D) + 32.0D;
        }

        @Override
        public double degreesKelvin() {
            return this.degrees() - Kelvin.ZERO_CELSIUS;
        }

        @Override
        public double degreesCelsius() {
            return this.degrees();
        }

        @Override
        public double degreesFahrenheit() {
            return Celsius.toFahrenheit(this.degrees());
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof final Temperature temperature) {
                return this.degreesCelsius() == temperature.degreesCelsius();
            }

            return false;
        }

    }

    record Fahrenheit(double degrees) implements Temperature {

        public static double toCelsius(final double fahrenheit) {
            return (fahrenheit - 32.0D) / 1.8D;
        }

        @Override
        public double degreesKelvin() {
            return this.degreesCelsius() - Kelvin.ZERO_CELSIUS;
        }

        @Override
        public double degreesCelsius() {
            return Fahrenheit.toCelsius(this.degrees());
        }

        @Override
        public double degreesFahrenheit() {
            return this.degrees();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof final Temperature temperature) {
                return this.degreesFahrenheit() == temperature.degreesFahrenheit();
            }

            return false;
        }

    }

}
