package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.architecturaldog.bluetools.content.BlueToolsRegistryKeys;
import net.architecturaldog.bluetools.content.material.MiningLevel;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.utility.BlueToolsHelper;
import net.minecraft.registry.RegistryKey;

public record MiningLevelProperty(MiningLevel level) implements MaterialProperty {

    private static final RegistryKey<MiningLevel> DEFAULT_LEVEL = RegistryKey
        .of(BlueToolsRegistryKeys.MINING_LEVEL, BlueToolsHelper.createIdentifier("none"));

    public static final MapCodec<MiningLevelProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance
            .group(BlueToolsResources.MINING_LEVEL.getCodec().fieldOf("level").forGetter(MiningLevelProperty::level))
            .apply(instance, MiningLevelProperty::new);
    });

    public static MiningLevelProperty getDefault() {
        return new MiningLevelProperty(
            BlueToolsResources.MINING_LEVEL.getEntry(MiningLevelProperty.DEFAULT_LEVEL).orElseThrow().value()
        );
    }

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.MINING_LEVEL.getValue();
    }

}
