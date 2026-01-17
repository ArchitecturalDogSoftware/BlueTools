package net.architecturaldog.bluetools.content.tool.property;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;

import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager.Entry;
import net.minecraft.util.dynamic.Codecs;

public record PartsProperty(
    Map<String, JsonResourceManager.Entry<Part>> parts,
    String headKey
) implements ToolProperty
{

    public static final MapCodec<PartsProperty> CODEC = RecordCodecBuilder
        .<PartsProperty>mapCodec(instance ->
        {
            final UnboundedMapCodec<String, Entry<Part>> partsMapCodec = Codec
                .unboundedMap(Codecs.NON_EMPTY_STRING, BlueToolsResources.PART.getEntryCodec());

            return instance
                .group(
                    partsMapCodec.fieldOf("parts").forGetter(PartsProperty::parts),
                    Codecs.NON_EMPTY_STRING.fieldOf("head_key").forGetter(PartsProperty::headKey)
                )
                .apply(instance, PartsProperty::new);
        })
        .validate(
            property -> property.parts().containsKey(property.headKey())
                ? DataResult.success(property)
                : DataResult.error(() -> "Part '%s' does not exist".formatted(property.headKey()))
        );

    @Override
    public ToolPropertyType<? extends ToolProperty> getType() {
        return BlueToolsToolPropertyTypes.PARTS.getValue();
    }

}
