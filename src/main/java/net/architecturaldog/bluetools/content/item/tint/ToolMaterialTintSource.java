package net.architecturaldog.bluetools.content.item.tint;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ToolMaterialTintSource(
    @NotNull String partName,
    @NotNull ColorProperty.PaletteColor paletteColor,
    int defaultColor
) implements TintSource
{

    public static final @NotNull MapCodec<ToolMaterialTintSource> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                Codecs.NON_EMPTY_STRING.fieldOf("part_name").forGetter(ToolMaterialTintSource::partName),
                ColorProperty.PaletteColor.CODEC
                    .fieldOf("palette_color")
                    .forGetter(ToolMaterialTintSource::paletteColor),
                Codecs.RGB.optionalFieldOf("default", 0xFFFFFFFF).forGetter(ToolMaterialTintSource::defaultColor)
            )
            .apply(instance, ToolMaterialTintSource::new)
        );

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return ToolMaterialTintSource.CODEC;
    }

    @Override
    public int getTint(final ItemStack stack, @Nullable final ClientWorld world, @Nullable final LivingEntity user) {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.TOOL_MATERIALS.getValue()))
            .flatMap(toolParts -> Optional.ofNullable(toolParts.entries().get(this.partName())))
            .flatMap(entry -> entry.value().getProperty(BlueToolsMaterialPropertyTypes.COLOR.getValue()))
            .map(property -> property.getColor(this.paletteColor()).integer())
            .orElseGet(this::defaultColor);
    }

}
