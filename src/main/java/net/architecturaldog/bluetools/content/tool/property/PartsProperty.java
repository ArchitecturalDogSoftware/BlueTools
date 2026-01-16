package net.architecturaldog.bluetools.content.tool.property;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;

import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager.Entry;
import net.minecraft.util.dynamic.Codecs;

public record PartsProperty(Map<String, JsonResourceManager.Entry<Part>> parts) implements ToolProperty {

    public static final MapCodec<PartsProperty> CODEC = RecordCodecBuilder
        .mapCodec(instance ->
        {
            final UnboundedMapCodec<String, Entry<Part>> unboundedMap = Codec
                .unboundedMap(Codecs.NON_EMPTY_STRING, BlueToolsResources.PART.getEntryCodec());

            return instance
                .group(unboundedMap.fieldOf("parts").forGetter(PartsProperty::parts))
                .apply(instance, PartsProperty::new);
        });

    @Override
    public ToolPropertyType<? extends ToolProperty> getType() {
        return BlueToolsToolPropertyTypes.PARTS.getValue();
    }

}
