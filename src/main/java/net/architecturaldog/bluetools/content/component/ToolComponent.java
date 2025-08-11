package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;
import net.architecturaldog.bluetools.content.tool.Tool;
import org.jetbrains.annotations.NotNull;

public record ToolComponent(@NotNull JsonResourceManager.Entry<Tool> toolEntry) {

    public static final @NotNull Codec<ToolComponent> CODEC =
        BlueToolsResources.TOOL.getEntryCodec().xmap(ToolComponent::new, ToolComponent::toolEntry);

}
