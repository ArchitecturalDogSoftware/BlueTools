package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.material.Temperature;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;

public record FluidProperty(Fluid fluid, Temperature meltingTemperature) implements MaterialProperty {

    public static final MapCodec<FluidProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                Registries.FLUID.getCodec().fieldOf("fluid").forGetter(FluidProperty::fluid),
                Temperature.CODEC.fieldOf("melting_temperature").forGetter(FluidProperty::meltingTemperature)
            )
            .apply(instance, FluidProperty::new);
    });

    @Override
    public MaterialPropertyType<FluidProperty> getType() {
        return BlueToolsMaterialPropertyTypes.FLUID.getValue();
    }

}
