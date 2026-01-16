package net.architecturaldog.bluetools.content.item.tint;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.component.MaterialComponent;
import net.architecturaldog.bluetools.content.material.property.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.material.property.ColorProperty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;

@Environment(EnvType.CLIENT)
public record MaterialTintSource(ColorProperty.PaletteColor paletteColor, int defaultColor) implements TintSource {

    public static final MapCodec<MaterialTintSource> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                ColorProperty.PaletteColor.CODEC.fieldOf("palette_color").forGetter(MaterialTintSource::paletteColor),
                Codecs.RGB.optionalFieldOf("default", 0xFFFFFFFF).forGetter(MaterialTintSource::defaultColor)
            )
            .apply(instance, MaterialTintSource::new);
    });

    private static Optional<ColorProperty> getMaterialTint(final MaterialComponent component) {
        return component.materialEntry().value().getProperty(BlueToolsMaterialPropertyTypes.COLOR.getValue());
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MaterialTintSource.CODEC;
    }

    @Override
    public int getTint(
        final ItemStack stack,
        final @Nullable ClientWorld world,
        final @Nullable LivingEntity user
    )
    {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.MATERIAL.getValue()))
            .flatMap(MaterialTintSource::getMaterialTint)
            .map(property -> property.getColor(this.paletteColor()).integer())
            .orElseGet(this::defaultColor);
    }

}
