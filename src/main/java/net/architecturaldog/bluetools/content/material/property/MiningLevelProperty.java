package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.BlueTools;
import net.architecturaldog.bluetools.content.BlueToolsRegistries;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

public record MiningLevelProperty(@NotNull MiningLevel level) implements MaterialProperty {

    private static final @NotNull RegistryKey<MiningLevel> DEFAULT_LEVEL =
        RegistryKey.of(BlueToolsRegistries.Keys.MINING_LEVEL, BlueTools.id("none"));

    public static final @NotNull MapCodec<MiningLevelProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(BlueToolsResources.MINING_LEVEL.getCodec().fieldOf("level").forGetter(MiningLevelProperty::level))
        .apply(instance, MiningLevelProperty::new)
    );

    public static @NotNull MiningLevelProperty getDefault() {
        return new MiningLevelProperty(
            BlueToolsResources.MINING_LEVEL.getEntry(MiningLevelProperty.DEFAULT_LEVEL).orElseThrow().value()
        );
    }

    @Override
    public @NotNull MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.MINING_LEVEL.getValue();
    }

}
