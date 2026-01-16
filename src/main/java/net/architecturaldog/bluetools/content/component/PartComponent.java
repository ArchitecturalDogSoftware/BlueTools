package net.architecturaldog.bluetools.content.component;

import com.mojang.serialization.Codec;

import net.architecturaldog.bluetools.content.part.Part;
import net.architecturaldog.bluetools.content.resource.BlueToolsResources;
import net.architecturaldog.bluetools.content.resource.JsonResourceManager;

public record PartComponent(JsonResourceManager.Entry<Part> partEntry) {

    public static final Codec<PartComponent> CODEC = BlueToolsResources.PART
        .getEntryCodec()
        .xmap(PartComponent::new, PartComponent::partEntry);

}
