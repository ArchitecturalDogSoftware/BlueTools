package net.architecturaldog.bluetools.content.tool.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record PartsProperty(@NotNull Map<String, JsonResourceManager.Entry<Part>> parts) implements ToolProperty {

    public static final @NotNull MapCodec<PartsProperty> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(Codec
            .unboundedMap(Codecs.NON_EMPTY_STRING, BlueToolsResources.PART.getEntryCodec())
            .fieldOf("parts")
            .forGetter(PartsProperty::parts))
        .apply(instance, PartsProperty::new));

    @Override
    public @NotNull ToolPropertyType<? extends ToolProperty> getType() {
        return BlueToolsToolPropertyTypes.PARTS.getValue();
    }

}
