package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.architecturaldog.bluetools.content.tool.Tool;

public record ToolComponent(JsonResourceManager.Entry<Tool> toolEntry) {

    public static final Codec<ToolComponent> CODEC = BlueToolsResources.TOOL
        .getEntryCodec()
        .xmap(ToolComponent::new, ToolComponent::toolEntry);

}
