package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.Temperature;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;

public record FluidProperty(@NotNull Fluid fluid, @NotNull Temperature meltingTemperature) implements MaterialProperty {

    public static final @NotNull MapCodec<FluidProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Registries.FLUID.getCodec().fieldOf("fluid").forGetter(FluidProperty::fluid),
        Temperature.CODEC.fieldOf("melting_temperature").forGetter(FluidProperty::meltingTemperature)
    ).apply(instance, FluidProperty::new));

    @Override
    public @NotNull MaterialPropertyType<FluidProperty> getType() {
        return BlueToolsMaterialPropertyTypes.FLUID.getValue();
    }

}
