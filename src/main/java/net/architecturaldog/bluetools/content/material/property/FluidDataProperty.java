package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.material.MaterialTemperature;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;

public record FluidDataProperty(Fluid fluid, MaterialTemperature meltingTemperature) implements MaterialProperty {

    public static final MapCodec<FluidDataProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            Registries.FLUID.getCodec().fieldOf("fluid").forGetter(FluidDataProperty::fluid),
            MaterialTemperature.CODEC.fieldOf("melting_temperature").forGetter(FluidDataProperty::meltingTemperature)
        )
        .apply(instance, FluidDataProperty::new));

    @Override
    public MaterialPropertyType<FluidDataProperty> getType() {
        return BlueToolsMaterialPropertyTypes.FLUID_DATA.getValue();
    }

}
