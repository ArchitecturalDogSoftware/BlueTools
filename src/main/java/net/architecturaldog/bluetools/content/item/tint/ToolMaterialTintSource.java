package net.architecturaldog.bluetools.content.item.tint;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.material.property.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.material.property.ColorProperty;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;

public record ToolMaterialTintSource(
    String partName,
    ColorProperty.PaletteColor paletteColor,
    int defaultColor
) implements TintSource
{

    public static final MapCodec<ToolMaterialTintSource> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(
                Codecs.NON_EMPTY_STRING.fieldOf("part_name").forGetter(ToolMaterialTintSource::partName),
                ColorProperty.PaletteColor.CODEC
                    .fieldOf("palette_color")
                    .forGetter(ToolMaterialTintSource::paletteColor),
                Codecs.RGB.optionalFieldOf("default", 0xFFFFFFFF).forGetter(ToolMaterialTintSource::defaultColor)
            )
            .apply(instance, ToolMaterialTintSource::new);
    });

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return ToolMaterialTintSource.CODEC;
    }

    @Override
    public int getTint(final ItemStack stack, @Nullable final ClientWorld world, @Nullable final LivingEntity user) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL_MATERIALS.getValue()))
            .flatMap(toolParts -> Optional.ofNullable(toolParts.entries().get(this.partName())))
            .flatMap(material -> material.value().getProperty(BlueToolsMaterialPropertyTypes.COLOR.getValue()))
            .map(property -> property.getColor(this.paletteColor()).integer())
            .orElseGet(this::defaultColor);
    }

}
