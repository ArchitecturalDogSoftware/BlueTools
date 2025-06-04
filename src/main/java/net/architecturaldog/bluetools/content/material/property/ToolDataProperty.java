package net.architecturaldog.bluetools.content.material.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.material.BlueToolsMaterialPropertyTypes;
import net.architecturaldog.bluetools.content.material.MaterialMiningLevel;
import net.architecturaldog.bluetools.content.resource.BlueToolsResourceReloadListeners;

import java.util.Optional;

public record ToolDataProperty(MaterialMiningLevel miningLevel, float speed, Optional<Integer> damagePerUse)
    implements MaterialProperty
{

    public static final MapCodec<ToolDataProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlueToolsResourceReloadListeners.MINING_LEVEL_MANAGER
            .getCodec()
            .fieldOf("mining_level")
            .forGetter(ToolDataProperty::miningLevel),
        Codec.floatRange(0.0F, Float.MAX_VALUE).fieldOf("speed").forGetter(ToolDataProperty::speed),
        Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("damage_per_use").forGetter(ToolDataProperty::damagePerUse)
    ).apply(instance, ToolDataProperty::new));

    @Override
    public MaterialPropertyType<? extends MaterialProperty> getType() {
        return BlueToolsMaterialPropertyTypes.TOOL_DATA.getValue();
    }

}
