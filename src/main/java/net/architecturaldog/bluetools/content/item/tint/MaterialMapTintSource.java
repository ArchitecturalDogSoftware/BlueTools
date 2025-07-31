package net.architecturaldog.bluetools.content.item.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.component.BlueToolsComponentTypes;
import net.architecturaldog.bluetools.content.material.property.BlueToolsMaterialPropertyTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public record MaterialMapTintSource(@NotNull String part, int defaultColor) implements TintSource {

    public static final @NotNull MapCodec<MaterialMapTintSource> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.NON_EMPTY_STRING.fieldOf("part").forGetter(MaterialMapTintSource::part),
            Codecs.RGB.fieldOf("default").forGetter(MaterialMapTintSource::defaultColor)
        ).apply(instance, MaterialMapTintSource::new));

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MaterialMapTintSource.CODEC;
    }

    @Override
    public int getTint(
        final @NotNull ItemStack stack,
        final @Nullable ClientWorld world,
        final @Nullable LivingEntity user
    )
    {
        return Optional
            .ofNullable(stack.get(BlueToolsComponentTypes.MATERIAL_MAP.getValue()))
            .flatMap(material -> Optional.ofNullable(material.materials().get(this.part())))
            .flatMap(material -> material.getProperty(BlueToolsMaterialPropertyTypes.COLOR.getValue()))
            .map(property -> property.color().integer())
            .orElseGet(this::defaultColor);
    }

}
