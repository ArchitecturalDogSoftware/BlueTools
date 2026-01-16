package net.architecturaldog.bluetools.content.component;

import java.util.Map;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.material.Material;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.minecraft.util.dynamic.Codecs;

public record ToolMaterialsComponent(
    Map<String, JsonResourceManager.Entry<Material>> entries
)
{

    public static final Codec<ToolMaterialsComponent> CODEC = Codec
        .unboundedMap(Codecs.NON_EMPTY_STRING, BlueToolsResources.MATERIAL.getEntryCodec())
        .xmap(ToolMaterialsComponent::new, ToolMaterialsComponent::entries);

}
