package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import org.jetbrains.annotations.NotNull;

public record MiningLevelProperty(@NotNull MiningLevel level) implements MaterialProperty {

    public static final @NotNull MapCodec<MiningLevelProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(BlueToolsResources.MINING_LEVEL.getCodec().fieldOf("level").forGetter(MiningLevelProperty::level))
        .apply(instance, MiningLevelProperty::new)
    );

    @Override
    public @NotNull MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.MINING_LEVEL.getValue();
    }

}
